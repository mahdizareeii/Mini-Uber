//
// Created by Mahdi Zareei on 9/27/25.
//

import Foundation
import ComposeApp

//for native screen graph scenarios

class SwiftUiRouter: BaseContractRouter {
    @Published var path: [AppRoute] = []
    
    func navigate(route: String) {
        //path.append(appRoute)
    }
    
    func navigateBack() {
        if !path.isEmpty {
            path.removeLast()
        }
    }
}
