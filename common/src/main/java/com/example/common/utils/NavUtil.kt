package com.example.common.utils

import android.app.Activity
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation

object NavUtil {

    private fun findNavController(@NonNull activity: Activity, @IdRes viewId: Int): NavController {
        return Navigation.findNavController(activity, viewId)
    }

    fun navigateTo(@NonNull activity: Activity, @IdRes viewId: Int, @IdRes resId: Int, bundle: Bundle?, navOptions: NavOptions?){
        val navController = findNavController(activity, viewId)
        navController.navigate(resId = resId, args = bundle, navOptions = navOptions)
    }

    fun navigateUp(@NonNull activity: Activity, @IdRes viewId: Int){
        val navController = findNavController(activity, viewId)
        navController.navigateUp()
    }

    fun clearBackStack(@NonNull activity: Activity, @IdRes viewId: Int, route: String): Boolean{
        val navController = findNavController(activity, viewId)
        return navController.clearBackStack(route = route)
    }

    fun clearBackStack(@NonNull activity: Activity, @IdRes viewId: Int, @IdRes destinationId: Int): Boolean{
        val navController = findNavController(activity, viewId)
        return navController.clearBackStack(destinationId = destinationId)
    }

    fun getBackQueue(@NonNull activity: Activity, @IdRes viewId: Int): ArrayDeque<NavBackStackEntry> {
        val navController = findNavController(activity, viewId)
        return navController.backQueue
    }
}