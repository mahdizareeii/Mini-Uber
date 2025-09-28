package com.shared.miniuber.feature.home

import com.shared.miniuber.core.base.BaseContract
import com.shared.miniuber.core.base.UiState
import kotlinx.coroutines.flow.update
import miniuber.composeapp.generated.resources.Res
import miniuber.composeapp.generated.resources.drop_off
import miniuber.composeapp.generated.resources.search_driver

class HomePresenter(
    private val interactor: HomeContract.Interactor,
    private val router: BaseContract.Router
) : HomeContract.Presenter() {

    private var homeState = HomeState()
        set(value) {
            field = value
            _state.update { UiState.Success(value) }
        }

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.NavigateBack -> router.navigateBack()
            is HomeEvent.OnMapStateUpdated -> {
                homeState = homeState.copy(cameraPosition = event.latLng)
            }

            is HomeEvent.CancelTravel -> homeState = homeState.copy(
                mapState = homeState.mapState.copy(startPoint = null, endPoint = null),
            )

            is HomeEvent.OnConfirmButtonClicked -> {
                when {
                    homeState.mapState.startPoint == null -> {
                        homeState = homeState.copy(
                            mapState = homeState.mapState.copy(startPoint = homeState.cameraPosition),
                            confirmButtonState = homeState.confirmButtonState.copy(
                                text = Res.string.drop_off
                            )
                        )
                    }

                    homeState.mapState.endPoint == null -> {
                        homeState = homeState.copy(
                            mapState = homeState.mapState.copy(endPoint = homeState.cameraPosition),
                            confirmButtonState = homeState.confirmButtonState.copy(
                                text = Res.string.search_driver
                            )
                        )
                    }
                }
            }
        }
    }
}