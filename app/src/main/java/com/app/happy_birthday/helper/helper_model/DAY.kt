package com.app.happy_birthday.helper.helper_model

enum class BirthdayPerson(
    val displayName: String,
    val hasImage: Boolean
) {
    BOYFRIEND("Boyfriend",true),
    GIRLFRIEND("Girlfriend", true),
    FRIENDS("Friends", true),
    BROTHER("Brother", true),
    SISTER("Sister", true),
    WIFE("Wife", true),
    DAD("Dad", true),
    MOTHER("Mother",true),
    KIDS("Kids", true),
    HUSBAND("Husband",true),
    GRANDPA("Grandpa",false),
    SON("Son", false),
    ME("Me", false),
    NONE("NA",false),
    PREMIUM("Premium",false)
}
