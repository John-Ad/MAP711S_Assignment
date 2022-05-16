package com.valentines.connection.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.valentines_garage.R
import com.valentines.connection.models.Car

class CarsListAdapter(context: Context, resource: Int, objects: MutableList<Car>) :
    ArrayAdapter<Car>(context, resource, objects) {

    private val mContext = context
    private val resourceLayout = resource
    val cars = objects

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView

        if (view == null) {
            val layoutInflater = LayoutInflater.from(mContext)
            view = layoutInflater.inflate(resourceLayout, null)
        }

        val car: Car? = getItem(position)

        if (car != null) {
            setData(car, view!!, position)
        }

        return view!!
    }

    private fun setData(car: Car, view: View, position: Int) {
        view.findViewById<TextView>(R.id.txt_car_vin).text = car.vin
        view.findViewById<TextView>(R.id.txt_car_brand).text = car.brand
        view.findViewById<TextView>(R.id.txt_car_name).text = car.name
        view.findViewById<TextView>(R.id.txt_car_color).text = car.color
    }
}