[Read the architecture article on Medium](https://medium.com/@mahdizareeii/building-a-mini-uber-with-kotlin-multiplatform-viper-architecture-compose-multiplatform-1d3dd0a08399)

# Mini-Uber — Adaptive Compose Multiplatform with VIPER-Inspired Architecture

| Original map experience | Adaptive Compose demo |
| :---: | :---: |
| **Map selection · Android and iOS**<br>[<img src="Screenshot_1.png" width="420" alt="Original map selection screens on Android and iOS">](Screenshot_1.png) | **Android · Pixel Fold**<br>[<img src="artifacts/adaptive-demo/android-adaptive.gif" width="420" alt="Pixel Fold opening, entering book posture, and closing onto the selected ride">](artifacts/adaptive-demo/android-adaptive.gif) |
| **Ride request · Android and iOS**<br>[<img src="Screenshot_2.png" width="420" alt="Original ride request screens on Android and iOS">](Screenshot_2.png) | **iOS · iPhone Duo in Device Hub**<br>[<img src="artifacts/adaptive-demo/ios-adaptive.gif" width="420" alt="Recorded cursor selecting iPhone Duo posture controls as the Compose layout adapts">](artifacts/adaptive-demo/ios-adaptive.gif) |

Select any preview to view it at full size.

Mini-Uber shares UI and presentation logic across Android and iOS with **Kotlin Multiplatform**, **Compose Multiplatform**, and **VIPER-inspired architecture**. Home opens the original map demo or a separate adaptive ride-history demo with local sample data. Immutable **StateFlow** state drives the UI; the adaptive layout responds to available space and native fold information while preserving selection and scroll positions.

## Architecture flow

```mermaid
flowchart LR
    View[Compose screen] -->|Events| Presenter
    Presenter --> Interactor
    Interactor --> Repository[DriverRepository]
    Repository --> Fake[FakeDriverRepositoryImpl]
    Presenter -->|StateFlow| View
    Presenter --> Router[Typed navigation router]
```

Screens send events to presenters. Presenters publish immutable state back to the UI, use interactors for data operations, and delegate typed navigation to routers.
