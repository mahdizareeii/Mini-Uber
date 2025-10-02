//
// Created by Mahdi Zareei on 9/27/25.
//

import Foundation
import ComposeApp

//This class only use for native screen graph scenarios
class SwiftUiRouter: BaseContractRouter {
    @Published var path: [AppScreens] = []
    
    func getResultFlow(key: String, initialValue: Any?) -> (any Kotlinx_coroutines_coreStateFlow)? {
        return nil
    }
    
    func getScreenArg(screen: any KotlinKClass) -> Any? {
        return nil
    }
    
    func navigate(route: Any) {
        //path.append(appRoute)
    }
    
    func navigate(route_ route: String) {
        
    }
    
    func navigate(route: Any, popUpTo: any KotlinKClass, popUpToInclusive: Bool) {
        
    }
    
    func navigate(route: String, popUpTo: String, popUpToInclusive_ popUpToInclusive: Bool) {
        
    }
    
    func navigateUp() {
        if !path.isEmpty {
            path.removeLast()
        }
    }
    
    func setParentResult(key: String, value: Any?) {
        
    }
}
