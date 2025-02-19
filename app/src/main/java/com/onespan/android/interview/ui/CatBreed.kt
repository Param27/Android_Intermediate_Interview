package com.onespan.android.interview.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.onespan.android.interview.viewmodel.CatBreedViewModel
import kotlinx.coroutines.launch
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.onespan.android.interview.MyApplication


@Composable
fun CatBreed(viewModel: CatBreedViewModel, snackbarHostState: SnackbarHostState) { // Correct: Two parameters
    val breeds by viewModel.breeds.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val application = context.applicationContext as MyApplication
    val connectivityChecker = application.connectivityChecker


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            // ... TopAppBar
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    if (connectivityChecker.isInternetAvailable()) { // Use connectivityChecker
                        isRefreshing = true
                        viewModel.fetchBreeds() // Call ViewModel function for refresh
                        isRefreshing = false
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("No internet connection")
                        }
                        isRefreshing = false
                    }
                }
            ) {
                when {
                    isLoading && !isRefreshing -> CircularProgressIndicator(
                        modifier = Modifier.size(
                            40.dp
                        )
                    )

                    errorMessage != null -> Text(
                        errorMessage!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )

                    breeds.isEmpty() -> Text("No breeds found.", Modifier.fillMaxSize())

                    else -> LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(breeds) { breed ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = breed.name,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "Origin: ${breed.origin}",
                                        color = Color(0xFF1565C0),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Country: ${breed.country}",
                                        color = Color(0xFF1565C0),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Coat: ${breed.coat}",
                                        color = Color(0xFF1565C0),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Pattern: ${breed.pattern}",
                                        color = Color(0xFF1565C0),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}




