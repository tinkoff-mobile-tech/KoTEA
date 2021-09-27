package ru.tinkoff.mobile.kotea.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.tinkoff.kotea.android.lifecycle.collectOnCreate
import ru.tinkoff.kotea.android.storeViaViewModel
import ru.tinkoff.mobile.kotea.sample.CounterEvent.CounterUiEvent
import ru.tinkoff.mobile.kotea.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val store by storeViaViewModel { CounterStore() }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        store.collectOnCreate(
            activity = this,
            uiStateMapper = CounterUiStateMapper(),
            stateCollector = ::collect,
            newsCollector = ::handle
        )
        binding.buttonStart.setOnClickListener { store.dispatch(CounterUiEvent.StartClicked) }
        binding.buttonStop.setOnClickListener { store.dispatch(CounterUiEvent.StopClicked) }
        binding.buttonReset.setOnClickListener { store.dispatch(CounterUiEvent.ResetClicked) }
    }

    private fun collect(state: CounterUiState) {
        binding.counterTextView.text = state.countText
        binding.counterProgressView.text = state.progressText
    }

    private fun handle(news: CounterNews) {
        when (news) {
            is CounterNews.ShowToast -> Toast.makeText(this, news.text, Toast.LENGTH_SHORT).show()
        }
    }
}