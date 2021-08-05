package softing.ubah4ukdev.popularlibrary.presenter

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import softing.ubah4ukdev.popularlibrary.views.IMainView
import softing.ubah4ukdev.popularlibrary.views.UsersScreen

/****
Project PopularLibrary
Package softing.ubah4ukdev.popularlibrary.presenter

Created by Ivan Sheynmaer

2021.08.05
v1.0
 */
class MainPresenter(
    private val router: Router,
) : MvpPresenter<IMainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(UsersScreen.create())
    }

    fun back() = router.exit()
}