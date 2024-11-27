package com.example.myactivityroomdb

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myactivityroomdb.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
class MainActivity : AppCompatActivity(), AddEditPersonFragment.AddEditPersonListener,
    PersonDetailAdapter.PersonalDetailsClickListener {

    private lateinit var binding: ActivityMainBinding
    private var dao: personDao? = null
    private lateinit var adapter: PersonDetailAdapter
    private lateinit var searchQueryLiveData: MutableLiveData<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVerse()
        attachUiListener()
        subscribeDataStremas()
    }
    private fun subscribeDataStremas() {
        searchQueryLiveData.observe(this) { query ->
            lifecycleScope.launch {
                    adapter.submitList( dao?.getSearchData(query)?.first())
            }

        }
        lifecycleScope.launch(Dispatchers.IO) {
            dao?.getAllData()?.collect { mList->
                adapter.submitList(mList)
                binding.searchcView.setQuery("",false)
                binding.searchcView.clearFocus()
                lifecycleScope.launch {
                    adapter.submitList( dao?.getSearchData(searchQueryLiveData.value ?: "" )?.first())
                }
            }
        }
    }
    private fun initVerse() {
        dao = AppDatabase.getDatabase(this)?.personDao()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PersonDetailAdapter(this)
        binding.recyclerView.adapter = adapter
        searchQueryLiveData = MutableLiveData("")
    }
    private fun attachUiListener() {
        binding.floatingActionButton.setOnClickListener {
            showBottomSheet()
        }
        binding.searchcView.setOnQueryTextListener(object : OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            @SuppressLint("SuspiciousIndentation")
            override fun onQueryTextChange(newText: String?): Boolean {
               if (newText != null)
                   onQueryChange(newText)
                   return true
               }
            })
    }
    private fun onQueryChange(query: String) {
        searchQueryLiveData.postValue(query)
        lifecycleScope.launch {
          if (query.isEmpty())
              adapter.submitList( dao?.getSearchData(query)?.first())
            else
                adapter.submitList(dao?.getSearchedExceptQueryData(query)?.first())
        }
    }
    private fun showBottomSheet(person: Person ?= null) {
        val bottomSheet = AddEditPersonFragment.newInstance(person)
        bottomSheet.show(supportFragmentManager, AddEditPersonFragment.TAG)
    }
    override fun onSaveBtnClicked(isUpdate: Boolean, person1: Person) {
        lifecycleScope .launch {
            if (isUpdate)
                dao?.updatePerson(person1)
             else
            dao?.savePerson(person1)
    }
}
    override fun onEditPersonalClick(person: Person) {
        showBottomSheet(person)
    }

    override fun onDeletePersonalClick(person: Person) {
       lifecycleScope.launch (Dispatchers.IO){
//           dao?.deletePerson(person)
           dao?.deletePersonById(person.pId)
       }
    }
}