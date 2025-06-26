package org.example.project


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import org.example.project.Navigation.Navigation
import org.example.project.data.TitleTopBarTypes
import org.koin.compose.KoinContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    PreComposeApp {
        KoinContext {

            val colors = getColorTheme()


            AppTheme {
                val navigator = rememberNavigator()
                val titleTopBar = getTitleTopAppBar(navigator)
                val isEditOrAddExpenses = titleTopBar != TitleTopBarTypes.DASHBOARD.values
                Scaffold(

                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = titleTopBar,
                                    fontSize = 25.sp,
                                    color = colors.textColor
                                )

                            },
                            navigationIcon = {
                                if (isEditOrAddExpenses) {
                                    IconButton(
                                        onClick = {
                                            navigator.popBackStack()
                                        }

                                    ) {
                                        Icon(
                                            modifier = Modifier.padding(start = 16.dp),
                                            imageVector = Icons.Default.ArrowBack,
                                            contentDescription = "Back Arrow",
                                            tint = colors.textColor
                                        )
                                    }
                                } else {
                                    Icon(
                                        modifier = Modifier.padding(start = 16.dp),
                                        imageVector = Icons.Default.Apps,
                                        contentDescription = "DashBoard icon",
                                        tint = colors.textColor
                                    )
                                }

                            }
                        )

                    },
                    floatingActionButton = {
                        if (!isEditOrAddExpenses) {
                            FloatingActionButton(
                                modifier = Modifier.padding(8.dp),
                                onClick = {
                                    navigator.navigate("/addExpenses")
                                },
                                shape = RoundedCornerShape(50),
                                containerColor = colors.addIconColor,
                                contentColor = Color.White,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Floating Icon",
                                    tint = Color.White
                                )
                            }
                        }

                    }) {
                    Navigation(navigator)
                }
            }
        }
    }
}
@Composable
fun getTitleTopAppBar(navigator: Navigator): String {
    var titleTopBar = TitleTopBarTypes.DASHBOARD

    val isOnAddExpenses =
        navigator.currentEntry.collectAsState(null).value?.route?.route.equals("/addExpenses/{id}?")
    if (isOnAddExpenses) {
        titleTopBar = TitleTopBarTypes.ADD_EXPENSES
    }

    val isOnEditExpenses = navigator.currentEntry.collectAsState(null).value?.path<Long>("id")
    isOnEditExpenses?.let {
        titleTopBar = TitleTopBarTypes.EDIT_EXPENSES
    }
    return titleTopBar.values
}