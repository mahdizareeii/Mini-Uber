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
//        let camera = GMSCameraPosition.camera(withLatitude: 0, longitude: 0, zoom: 1)
//        mapView = GMSMapView.map(withFrame: view.bounds, camera: camera)
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        mapView.delegate = self
        view.addSubview(mapView)
    }
    
    func updateState(state: MapState) {
        self.state = state
        mapView.clear()
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
}

