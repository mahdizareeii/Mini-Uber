import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    let mapsConfigured: Bool

    func makeUIViewController(context: Context) -> UIViewController {
        AdaptiveHostViewController(mapsConfigured: mapsConfigured)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    let mapsConfigured: Bool

    var body: some View {
        ComposeView(mapsConfigured: mapsConfigured)
            .ignoresSafeArea()
    }
}
