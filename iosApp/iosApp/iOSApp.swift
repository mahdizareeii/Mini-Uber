import SwiftUI
import ComposeApp
import GoogleMaps

// A static initializer runs once even if SwiftUI recreates the App value.
private enum AppBootstrap {
    static let mapsConfigured: Bool = {
        PlatformKt.doInitKoin { _ in }
        let key = (Bundle.main.object(forInfoDictionaryKey: "MapsAPIKey") as? String)?
            .trimmingCharacters(in: .whitespacesAndNewlines) ?? ""
        guard !key.isEmpty, !key.contains("$("), key != "YOUR_IOS_MAPS_API_KEY" else {
            return false
        }
        return GMSServices.provideAPIKey(key)
    }()
}

@main
struct iOSApp: App {
    init() {
        _ = AppBootstrap.mapsConfigured
    }

    var body: some Scene {
        WindowGroup {
            ContentView(mapsConfigured: AppBootstrap.mapsConfigured)
        }
    }
}
