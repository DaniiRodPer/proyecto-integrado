package com.dam.proydrp.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dam.proydrp.R
import com.dam.proydrp.data.model.AccommodationTag
import com.dam.proydrp.data.model.UserTag

@Composable
fun getUserTagLabel(userTag: UserTag): String {
    return when (userTag) {
        UserTag.NON_SMOKER -> stringResource(R.string.tag_non_smoker)
        UserTag.SMOKER -> stringResource(R.string.tag_smoker)
        UserTag.VEGETARIAN -> stringResource(R.string.tag_vegetarian)
        UserTag.SPORTY -> stringResource(R.string.tag_sporty)
        UserTag.CHILL -> stringResource(R.string.tag_chill)
        UserTag.PARTY_GOER -> stringResource(R.string.tag_party_goer)
        UserTag.PET_OWNER -> stringResource(R.string.tag_pet_owner)
        UserTag.VEGAN -> stringResource(R.string.tag_vegan)
        UserTag.FITNESS_ENTHUSIAST -> stringResource(R.string.tag_fitness_enthusiast)
        UserTag.EARLY_BIRD -> stringResource(R.string.tag_early_bird)
        UserTag.NIGHT_OWL -> stringResource(R.string.tag_night_owl)
        UserTag.GAMER -> stringResource(R.string.tag_gamer)
        UserTag.CLEAN_FREAK -> stringResource(R.string.tag_clean_freak)
        UserTag.EXTROVERT -> stringResource(R.string.tag_extrovert)
        UserTag.INTROVERT -> stringResource(R.string.tag_introvert)
        UserTag.OUTDOORSY -> stringResource(R.string.tag_outdoorsy)
        UserTag.TRAVELER -> stringResource(R.string.tag_traveler)
        UserTag.QUIET -> stringResource(R.string.tag_quiet)
    }
}