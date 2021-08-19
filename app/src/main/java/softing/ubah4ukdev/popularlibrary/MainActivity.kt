package softing.ubah4ukdev.popularlibrary

import android.os.Bundle
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import softing.ubah4ukdev.popularlibrary.domain.network.NetworkState
import softing.ubah4ukdev.popularlibrary.domain.network.NetworkStateObservable
import softing.ubah4ukdev.popularlibrary.presenter.abs.AbsActivity
import softing.ubah4ukdev.popularlibrary.presenter.convert.ConvertScreen
import softing.ubah4ukdev.popularlibrary.presenter.users.UsersScreen
import softing.ubah4ukdev.populatelibrary.R
import softing.ubah4ukdev.populatelibrary.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainActivity : AbsActivity(R.layout.activity_main) {

    private val vb: ActivityMainBinding by viewBinding()

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    //private val presenter by moxyPresenter { MainPresenter(router) }
    private val navigator = AppNavigator(this, R.id.container)

    val disposables = CompositeDisposable()

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState ?: let {
            router.newRootScreen(UsersScreen)
            init()
        }
    }

    private fun init() {
        vb.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_users -> {
                    router.replaceScreen(UsersScreen)
                    true
                }
                R.id.navigation_convert -> {
                    router.replaceScreen(ConvertScreen())
                    true
                }
                else -> false
            }
        }

        router.replaceScreen(UsersScreen)

        val connect =
            NetworkStateObservable(this)
                .publish()

        connect.connect()

        disposables +=
            connect.delay(1L, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe { onNext(1, it) }
    }

    private fun onNext(no: Int, state: NetworkState) {
        Toast.makeText(this, "$no: NetworkState: $state", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()

        disposables.dispose()
    }

}