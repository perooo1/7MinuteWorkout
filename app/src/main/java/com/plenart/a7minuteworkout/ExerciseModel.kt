package com.plenart.a7minuteworkout

class ExerciseModel(private var id: Int, private var name: String, private var image: Int, private var idCompleted: Boolean, private var isSelected: Boolean) {

    fun getId(): Int{
        return this.id;
    }

    fun setId(id:Int){
        this.id = id;
    }



}