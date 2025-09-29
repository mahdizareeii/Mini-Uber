import UIKit
import SwiftUI
import ComposeApp
import GoogleMaps

struct ComposeView: UIViewControllerRepresentable {

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(
            mapUiViewController: { state, camera in
                return MapViewController(state: state, onCameraChanged: camera)
            }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

struct ContentView: View {
    var body: some View {
        initGoogleMap()
        initKoinSetup()
        ComposeView()
            .ignoresSafeArea()
    }
}

private func initGoogleMap() -> some View {
    GMSServices.provideAPIKey("AIzaSyAtRVvG3Be3xXiZFR7xp-K-9hy4nZ4hMFs")
    return EmptyView()
}

private func initKoinSetup() -> some View {
    PlatformKt.doInitKoin { koinContext in }
    return EmptyView()
}



