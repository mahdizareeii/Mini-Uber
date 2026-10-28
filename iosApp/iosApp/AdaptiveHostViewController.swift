import UIKit
import ComposeApp

/// Retains one Compose controller and one shared window state across UIKit layout updates.
final class AdaptiveHostViewController: UIViewController {
    private let adaptiveWindowState = AdaptiveWindowState()
    private let mapsConfigured: Bool
    private var composeController: UIViewController!

    init(mapsConfigured: Bool) {
        self.mapsConfigured = mapsConfigured
        super.init(nibName: nil, bundle: nil)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func loadView() {
        let hostView = AdaptiveHostView()
        let state = adaptiveWindowState
        hostView.onRegionsChanged = { regions in
            state.updateRegions(regions: regions)
        }
        view = hostView
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        composeController = MainViewControllerKt.MainViewController(
            mapsConfigured: mapsConfigured,
            adaptiveWindowState: adaptiveWindowState,
            mapUiViewController: { state, camera in
                MapViewController(state: state, onCameraChanged: camera)
            }
        )
        addChild(composeController)
        composeController.view.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(composeController.view)
        NSLayoutConstraint.activate([
            composeController.view.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            composeController.view.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            composeController.view.topAnchor.constraint(equalTo: view.topAnchor),
            composeController.view.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
        composeController.didMove(toParent: self)
    }
}

private final class AdaptiveHostView: UIView {
    var onRegionsChanged: (([AdaptiveRegion]) -> Void)?
    private var lastWindowFrame: CGRect?

    override func didMoveToWindow() {
        super.didMoveToWindow()
        setNeedsUpdateConstraints()
    }

    override func safeAreaInsetsDidChange() {
        super.safeAreaInsetsDidChange()
        setNeedsUpdateConstraints()
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        let windowFrame = window.map { convert(bounds, to: $0) }
        if windowFrame != lastWindowFrame {
            lastWindowFrame = windowFrame
            setNeedsUpdateConstraints()
        }
    }

    override func updateConstraints() {
        // Reading here lets UIKit observe reserved-region changes and invalidate this pass.
        // UIKit points match Compose dp on iOS; safe-area insets are handled by Compose.
        var regions: [AdaptiveRegion] = []
        if #available(iOS 27.1, *), let window {
            for region in reservedRegions(kind: .division) where region.isActive {
                regions.append(sharedRegion(frame: convert(region.frame, to: window), kind: .division))
            }
            for region in reservedRegions(kind: .occlusion) where region.isActive {
                regions.append(sharedRegion(frame: convert(region.frame, to: window), kind: .occlusion))
            }
        }
        onRegionsChanged?(regions)
        super.updateConstraints()
    }

    private func sharedRegion(frame: CGRect, kind: AdaptiveRegionKind) -> AdaptiveRegion {
        AdaptiveRegion(
            left: Float(frame.minX),
            top: Float(frame.minY),
            right: Float(frame.maxX),
            bottom: Float(frame.maxY),
            kind: kind
        )
    }
}
