package com.demo.app.countries_list.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import com.demo.app.shared.AppContainer
import com.demo.app.countries_list.data.local.entity.CountryEntity
import com.demo.app.countries_list.presentation.adapter.CountryListAdapter
import com.demo.app.databinding.CountryListFragmentBinding

/**
 * A fragment representing a list of Items.
 */
class CountriesListFragment : Fragment() {

    private val TAG = CountriesListFragment::class.java.simpleName
    private lateinit var _viewModel: CountriesListViewModel
    private lateinit var _countryListAdapter: CountryListAdapter
    private lateinit var _fragmentContainerView: CountryListFragmentBinding

    private lateinit var _bindingRecyclerView: RecyclerView
    private lateinit var _bindingProgressView: ProgressBar
    private lateinit var _bindingErrorTextView: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "onCreateView")
        _fragmentContainerView = CountryListFragmentBinding.inflate(layoutInflater)
        _bindingRecyclerView = _fragmentContainerView.viewCountryList
        _bindingProgressView = _fragmentContainerView.loadingView
        _bindingErrorTextView = _fragmentContainerView.errorView
        _bindingRecyclerView.visibility = View.INVISIBLE
        _bindingProgressView.visibility = View.VISIBLE
        _bindingErrorTextView.visibility = View.INVISIBLE

        return _fragmentContainerView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        val appContainer: AppContainer = AppContainer.getInstance()
        appContainer?.let {
            _viewModel = appContainer.provideCountryListViewModel(this)
            _countryListAdapter = CountryListAdapter()
            _bindingRecyclerView.layoutManager = LinearLayoutManager(context)
            _bindingRecyclerView.addItemDecoration(
                DividerItemDecoration(
                    _bindingRecyclerView.context,
                    DividerItemDecoration.VERTICAL
                )
            )
            _bindingRecyclerView.adapter = _countryListAdapter

            _viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
                updateUi(uiState)
            }
        }
    }

    private fun updateUi(countriesListViewState: CountriesListViewState){
        when (countriesListViewState) {
            is CountriesListViewState.Loading -> showLoading()
            is CountriesListViewState.Success -> showData(countriesListViewState.data)
            is CountriesListViewState.LocalData -> showData(countriesListViewState.data)
            is CountriesListViewState.Error -> showError(countriesListViewState.message)
        }
    }

    private fun showLoading() {
        _bindingRecyclerView.visibility = View.INVISIBLE
        _bindingProgressView.visibility = View.VISIBLE
        _bindingErrorTextView.visibility = View.INVISIBLE
    }

    private fun showData(data: List<CountryEntity>) {
        _bindingRecyclerView.visibility = View.VISIBLE
        _bindingProgressView.visibility = View.INVISIBLE
        _bindingErrorTextView.visibility = View.INVISIBLE
        _countryListAdapter.submitList(data)
    }

    private fun showError(errorMessage: String) {
        _bindingRecyclerView.visibility = View.INVISIBLE
        _bindingProgressView.visibility = View.INVISIBLE
        _bindingErrorTextView.visibility = View.VISIBLE
        _bindingErrorTextView.text = errorMessage
    }
}