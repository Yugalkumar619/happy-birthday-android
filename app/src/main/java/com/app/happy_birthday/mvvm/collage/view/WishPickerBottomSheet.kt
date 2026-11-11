package com.app.happy_birthday.mvvm.collage.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.happy_birthday.R
import com.app.happy_birthday.databinding.BottomSheetCollageWishPickerBinding
import com.app.happy_birthday.helper.Extensions.getCategoryData
import com.app.happy_birthday.helper.LocaleHelper.getAllBhajan
import com.app.happy_birthday.helper.helper_model.BirthdayPerson
import com.app.happy_birthday.mvvm.collage.adapter.WishPickerAdapter
import com.app.happy_birthday.mvvm.collage.view_model.CollageViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class WishPickerBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetCollageWishPickerBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CollageViewModel

    private val wishAdapter = WishPickerAdapter { wish ->
        viewModel.selectWish(wish)
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetCollageWishPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireParentFragment())[CollageViewModel::class.java]
        binding.rvWishes.adapter = wishAdapter
        setUpPeople()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setUpPeople() {
        val people = requireContext().getCategoryData().map { it.day }
        val selected = viewModel.selectedPerson ?: people.first()

        people.forEach { person ->
            val chip = Chip(requireContext()).apply {
                id = View.generateViewId()
                text = person.displayName
                isCheckable = true
                isCheckedIconVisible = false
                chipBackgroundColor =
                    ContextCompat.getColorStateList(context, R.color.collage_chip_background)
                setTextColor(ContextCompat.getColorStateList(context, R.color.collage_chip_text))
                isChecked = person == selected
                setOnClickListener { selectPerson(person) }
            }
            binding.chipGroupPeople.addView(chip)
        }
        showWishes(selected)
    }

    private fun selectPerson(person: BirthdayPerson) {
        viewModel.selectedPerson = person
        showWishes(person)
        binding.rvWishes.scrollToPosition(0)
    }

    private fun showWishes(person: BirthdayPerson) {
        wishAdapter.submitList(getAllBhajan(person).mapNotNull { it.description })
    }

    companion object {
        const val TAG = "WishPickerBottomSheet"
    }
}
