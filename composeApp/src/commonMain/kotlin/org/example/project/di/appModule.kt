package org.example.project.di

import com.expenseApp.db.AppDatabase
import org.example.project.data.ExpenseManager
import org.example.project.data.ExpensesRepoImpl
import org.example.project.domain.ExpenseRepository
import org.example.project.presentation.ExpensesViewModel
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

fun appModule(appDatabase: AppDatabase) = module {
    single { ExpenseManager}.withOptions { createdAtStart() }  // Se crea el manager como singleton para que pueda usarse en toda la app
    single<ExpenseRepository>{ExpensesRepoImpl(get(), appDatabase)} // se crea el repositorio y se le pasa el manager con el GET()
    factory{ ExpensesViewModel(get()) } // se genera el ViewModel con el repository y el manager
}