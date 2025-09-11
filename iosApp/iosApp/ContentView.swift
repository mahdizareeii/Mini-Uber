import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {


    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

struct ContentView: View {
    var body: some View {
        initKoinSetup()
        ComposeView()
            .ignoresSafeArea()
    }
}

private func initKoinSetup() -> some View {
    PlatformKt.doInitKoin { koinContext in }
    return EmptyView()
}



