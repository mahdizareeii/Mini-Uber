import UIKit
import GoogleMaps
import ComposeApp

final class MapViewController: UIViewController, KmpMap, GMSMapViewDelegate {
    private struct Point: Equatable {
        let latitude: Double
        let longitude: Double

        var coordinate: CLLocationCoordinate2D {
            CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
        }
    }

    private struct Selection: Equatable {
        let start: Point?
        let end: Point?
    }

    private var mapView: GMSMapView!
    private var state: MapState
    private let onCameraChanged: (MapState.LatLng) -> KotlinUnit
    private let startMarker = GMSMarker()
    private let endMarker = GMSMarker()
    private let routePolyline = GMSPolyline()
    private var renderedSelection: Selection?
    private var routePoints: [CLLocationCoordinate2D] = []
    private var routeAnimation: Task<Void, Never>?

    init(state: MapState, onCameraChanged: @escaping (MapState.LatLng) -> KotlinUnit) {
        self.state = state
        self.onCameraChanged = onCameraChanged
        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    deinit {
        routeAnimation?.cancel()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        let options = GMSMapViewOptions()
        options.frame = view.bounds
        options.camera = GMSCameraPosition.camera(
            withLatitude: state.cameraPosition.latitude,
            longitude: state.cameraPosition.longitude,
            zoom: state.cameraZoom
        )
        mapView = GMSMapView(options: options)
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        mapView.delegate = self
        view.addSubview(mapView)

        startMarker.icon = GMSMarker.markerImage(with: .green)
        endMarker.icon = GMSMarker.markerImage(with: .red)
        routePolyline.strokeWidth = 6
        routePolyline.strokeColor = .black
        routePolyline.geodesic = true
        renderSelection()
    }

    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        routeAnimation?.cancel()
        routeAnimation = nil
        // A temporarily hidden map should not retain a partially drawn route.
        routePolyline.path = makePath(routePoints)
    }

    func updateState(state: MapState) {
        self.state = state
        guard isViewLoaded else { return }
        renderSelection()
    }

    private func renderSelection() {
        updateMarker(startMarker, data: state.startPoint)
        updateMarker(endMarker, data: state.endPoint)
        let selection = Selection(start: point(state.startPoint), end: point(state.endPoint))
        guard selection != renderedSelection else { return }
        renderedSelection = selection

        routeAnimation?.cancel()
        routeAnimation = nil
        routePoints = []
        routePolyline.path = nil
        routePolyline.map = nil

        if let start = selection.start, let end = selection.end {
            // This curve illustrates the selected endpoints; it is not a road route.
            routePoints = generateCurvePoints(start: start.coordinate, end: end.coordinate)
            routePolyline.map = mapView
            animateRoute(routePoints)
            let bounds = GMSCoordinateBounds(coordinate: start.coordinate, coordinate: end.coordinate)
            mapView.animate(with: GMSCameraUpdate.fit(bounds, withPadding: 100))
        } else if let point = selection.start ?? selection.end {
            mapView.animate(with: GMSCameraUpdate.setTarget(point.coordinate, zoom: state.cameraZoom))
        }
    }

    private func updateMarker(_ marker: GMSMarker, data: MapState.MarkerData?) {
        guard let data else {
            marker.map = nil
            return
        }
        marker.position = CLLocationCoordinate2D(
            latitude: data.latLng.latitude,
            longitude: data.latLng.longitude
        )
        marker.title = data.title
        marker.snippet = data.snipped
        marker.map = mapView
    }

    private func point(_ marker: MapState.MarkerData?) -> Point? {
        guard let marker else { return nil }
        return Point(latitude: marker.latLng.latitude, longitude: marker.latLng.longitude)
    }

    func mapView(_ mapView: GMSMapView, didChange position: GMSCameraPosition) {
        emitCameraPosition(position)
    }

    func mapView(_ mapView: GMSMapView, idleAt position: GMSCameraPosition) {
        emitCameraPosition(position)
    }

    private func emitCameraPosition(_ position: GMSCameraPosition) {
        _ = onCameraChanged(MapState.LatLng(
            latitude: position.target.latitude,
            longitude: position.target.longitude
        ))
    }

    private func animateRoute(_ points: [CLLocationCoordinate2D]) {
        routeAnimation = Task { @MainActor [weak self] in
            let path = GMSMutablePath()
            for point in points {
                guard !Task.isCancelled, self != nil else { return }
                path.add(point)
                self?.routePolyline.path = path
                do {
                    try await Task.sleep(nanoseconds: 10_000_000)
                } catch {
                    return
                }
            }
        }
    }

    private func makePath(_ points: [CLLocationCoordinate2D]) -> GMSPath {
        let path = GMSMutablePath()
        for point in points {
            path.add(point)
        }
        return path
    }

    private func generateCurvePoints(
        start: CLLocationCoordinate2D,
        end: CLLocationCoordinate2D,
        curveHeight: Double = 0.2,
        numPoints: Int = 100
    ) -> [CLLocationCoordinate2D] {
        let midLat = (start.latitude + end.latitude) / 2
        let midLng = (start.longitude + end.longitude) / 2
        let controlLat = midLat - (end.longitude - start.longitude) * curveHeight
        let controlLng = midLng + (end.latitude - start.latitude) * curveHeight
        return (0...numPoints).map { index in
            let t = Double(index) / Double(numPoints)
            return CLLocationCoordinate2D(
                latitude: pow(1 - t, 2) * start.latitude + 2 * (1 - t) * t * controlLat + pow(t, 2) * end.latitude,
                longitude: pow(1 - t, 2) * start.longitude + 2 * (1 - t) * t * controlLng + pow(t, 2) * end.longitude
            )
        }
    }
}
