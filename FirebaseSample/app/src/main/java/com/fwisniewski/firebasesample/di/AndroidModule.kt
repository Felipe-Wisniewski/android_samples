package com.fwisniewski.firebasesample.di

import com.fwisniewski.firebasesample.auth.AuthManager
import org.koin.dsl.module

val androidModule = module {

    single {
        AuthManager(context = get())
    }
}