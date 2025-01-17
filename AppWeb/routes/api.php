<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\UserListController;
use App\Http\Controllers\Api\ListMediaController;
use App\Http\Controllers\Api\RatingController;
use App\Http\Controllers\Api\UserController;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');

Route::apiResource('user/list', UserController::class)->except([
    'create', 'edit'
]);

Route::get('/userList/{userId}/lists', [UserListController::class, 'getByUser']);

Route::apiResource('userList', UserListController::class)->except([
    'create', 'edit'
]);

Route::apiResource('listMedia', ListMediaController::class)->except([
    'create', 'edit'
]);

Route::apiResource('rating', RatingController::class)->except([
    'create', 'edit'
]);