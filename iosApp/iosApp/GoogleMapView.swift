//
// Created by Mahdi Zareei on 9/28/25.
//

import Foundation
import GoogleMaps
import SwiftUI
import ComposeApp

class MapViewController: UIViewController, KmpMap, GMSMapViewDelegate {
    private var mapView: GMSMapView!
    private var state: MapState
    private let onCameraChanged: (MapState.LatLng) -> KotlinUnit
    
    init(
        state: MapState,
        onCameraChanged: @escaping (MapState.LatLng) -> KotlinUnit
    ) {
        self.state = state
        self.onCameraChanged = onCameraChanged
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        let camera = GMSCameraPosition.camera(
            withLatitude: state.cameraPosition.latitude,
            longitude: state.cameraPosition.longitude,
            zoom: state.cameraZoom
        )
        mapView = GMSMapView.map(withFrame: view.bounds, camera: camera)
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        mapView.delegate = self
        view.addSubview(mapView)
    }
    
    func updateState(state: MapState) {
        self.state = state
        mapView.clear()
        
        
        if let start = state.startPoint, let end = state.endPoint {
            // Add start & end markers
            let startMarker = GMSMarker(
                position: CLLocationCoordinate2D(
                    latitude: start.latLng.latitude,
                    longitude: start.latLng.longitude
                )
            )
            startMarker.icon = GMSMarker.markerImage(with: .green)
            startMarker.map = mapView
            
            let endMarker = GMSMarker(
                position: CLLocationCoordinate2D(
                    latitude: end.latLng.latitude,
                    longitude: end.latLng.longitude
                )
            )
            endMarker.icon = GMSMarker.markerImage(with: .red)
            endMarker.map = mapView
            
            let curvePoints = generateCurvePoints(
                start: CLLocationCoordinate2D(
                    latitude: start.latLng.latitude,
                    longitude: start.latLng.longitude
                ),
                end: CLLocationCoordinate2D(
                    latitude: end.latLng.latitude,
                    longitude: end.latLng.longitude
                ),
                curveHeight: 0.2
            )
            
            let polyline = drawCurve(on: mapView, points: [])
            
            animatePolyline(polyline: polyline, points: curvePoints)
            
            let bounds = GMSCoordinateBounds(
                coordinate: CLLocationCoordinate2D(
                    latitude: start.latLng.latitude,
                    longitude: start.latLng.longitude
                ),
                coordinate: CLLocationCoordinate2D(
                    latitude: end.latLng.latitude,
                    longitude: end.latLng.longitude
                )
            )
            mapView.animate(with: GMSCameraUpdate.fit(bounds, withPadding: 100))
        }
        
        
        var bounds = GMSCoordinateBounds()
        
        if let start = state.startPoint {
            let startMarker = GMSMarker(
                position: CLLocationCoordinate2D(
                    latitude: start.latLng.latitude,
                    longitude: start.latLng.longitude
                )
            )
            startMarker.icon = GMSMarker.markerImage(with: .green)
            startMarker.title = start.title
            startMarker.snippet = start.snipped
            startMarker.map = mapView
            bounds = bounds.includingCoordinate(startMarker.position)
        }
        
        if let end = state.endPoint {
            let endMarker = GMSMarker(
                position: CLLocationCoordinate2D(
                    latitude: end.latLng.latitude,
                    longitude: end.latLng.longitude
                )
            )
            endMarker.icon = GMSMarker.markerImage(with: .red)
            endMarker.title = end.title
            endMarker.snippet = end.snipped
            endMarker.map = mapView
            bounds = bounds.includingCoordinate(endMarker.position)
        }
        
        if state.startPoint != nil && state.endPoint != nil {
            mapView.animate(with: GMSCameraUpdate.fit(bounds, withPadding: 100))
        } else if let start = state.startPoint {
            mapView.animate(
                with: GMSCameraUpdate.setTarget(
                    CLLocationCoordinate2D(
                        latitude: start.latLng.latitude,
                        longitude: start.latLng.longitude
                    ),
                    zoom: state.cameraZoom
                )
            )
        } else if let end = state.endPoint {
            mapView.animate(
                with: GMSCameraUpdate.setTarget(
                    CLLocationCoordinate2D(
                        latitude: end.latLng.latitude,
                        longitude: end.latLng.longitude
                    ),
                    zoom: state.cameraZoom
                )
            )
        }
    }
    
    func mapView(_ mapView: GMSMapView, didChange position: GMSCameraPosition) {
        let latLng = MapState.LatLng(
            latitude: position.target.latitude,
            longitude: position.target.longitude
        )
        _ = onCameraChanged(latLng)
    }
    
    
    func mapView(_ mapView: GMSMapView, idleAt position: GMSCameraPosition) {
        let latLng = MapState.LatLng(
            latitude: position.target.latitude,
            longitude: position.target.longitude
        )
        _ = onCameraChanged(latLng)
    }
    
    func animatePolyline(
        polyline: GMSPolyline,
        points: [CLLocationCoordinate2D],
        interval: TimeInterval = 0.01
    ) {
        let path = GMSMutablePath()
        polyline.path = path
        
        DispatchQueue.global(qos: .userInteractive).async {
            for point in points {
                DispatchQueue.main.async {
                    path.add(point)
                    polyline.path = path
                }
                Thread.sleep(forTimeInterval: interval)
            }
        }
    }
    
    func drawCurve(on mapView: GMSMapView, points: [CLLocationCoordinate2D]) -> GMSPolyline {
        let path = GMSMutablePath()
        for point in points {
            path.add(point)
        }
        let polyline = GMSPolyline(path: path)
        polyline.strokeWidth = 6
        polyline.strokeColor = UIColor.black
        polyline.geodesic = true
        polyline.map = mapView
        return polyline
    }
    
    func generateCurvePoints(
        start: CLLocationCoordinate2D,
        end: CLLocationCoordinate2D,
        curveHeight: Double = 0.1,
        numPoints: Int = 100
    ) -> [CLLocationCoordinate2D] {
        var points: [CLLocationCoordinate2D] = []
        
        let midLat = (start.latitude + end.latitude) / 2
        let midLng = (start.longitude + end.longitude) / 2
        
        let dx = end.longitude - start.longitude
        let dy = end.latitude - start.latitude
        
        let controlLat = midLat + (-dx * curveHeight)
        let controlLng = midLng + (dy * curveHeight)
        
        for i in 0...numPoints {
            let t = Double(i) / Double(numPoints)
            let lat = pow(1 - t, 2) * start.latitude + 2 * (1 - t) * t * controlLat + pow(t, 2) * end.latitude
            let lng = pow(1 - t, 2) * start.longitude + 2 * (1 - t) * t * controlLng + pow(t, 2) * end.longitude
            points.append(CLLocationCoordinate2D(latitude: lat, longitude: lng))
        }
        
        return points
    }
    
    
}

