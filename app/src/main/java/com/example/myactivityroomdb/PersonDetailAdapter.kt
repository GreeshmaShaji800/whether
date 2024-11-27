package com.example.myactivityroomdb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myactivityroomdb.databinding.SingleItemBinding

class PersonDetailAdapter(private val listener: PersonalDetailsClickListener) : ListAdapter <Person,PersonDetailAdapter.PersonDetailViewHolder>(DiffUtilCallback()) {

    inner class PersonDetailViewHolder(private val binding: SingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            init {
                binding.editBtn.setOnClickListener {
                    listener.onEditPersonalClick(getItem(adapterPosition))
                }
                binding.deleteBtn.setOnClickListener {
                    listener.onDeletePersonalClick(getItem(adapterPosition))
                }
            }

            fun bind(person: Person) {
                binding.personNameTv.text = person.name
                binding.personAgeTv.text = person.age.toString()
                binding.personCityTv.text = person.city
            }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person) = oldItem.pId == newItem.pId

        override fun areContentsTheSame(oldItem: Person, newItem: Person) = oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonDetailViewHolder {
       return PersonDetailViewHolder(SingleItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PersonDetailViewHolder, position: Int) {
       holder.bind(getItem(position))
    }

    interface PersonalDetailsClickListener{

        fun onEditPersonalClick(person: Person)
        fun onDeletePersonalClick(person: Person)
    }
}