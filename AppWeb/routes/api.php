<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\UserListController;
use App\Http\Controllers\Api\ListMediaController;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');

Route::apiResource('userList', UserListController::class)->except([
    'create', 'edit'
]);
Route::apiResource('listMedia', ListMediaController::class)->except([
    'create', 'edit'
]);
Route::apiResource('rating', ListMediaController::class)->except([
    'create', 'edit'
]);
