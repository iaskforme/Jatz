package com.project.jatz.view.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import com.project.jatz.R

/**
 * Class that inherits from DialogFragment and contains the parameters that allow the future creation of one.In this case for the creation of a board.
 */
class CreateBoardFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material)

        val view = inflater.inflate(R.layout.fragment_create_board, container, false)


        return view
    }

}