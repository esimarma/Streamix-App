<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\UserListController;
use App\Http\Controllers\Api\ListMediaController;
use App\Http\Controllers\Api\RatingController;
use App\Http\Controllers\Api\UserController;
use App\Http\Controllers\Auth\RegisteredUserController;
use App\Http\Controllers\Auth\AuthenticatedSessionController;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');

Route::post('/register', [RegisteredUserController::class, 'storeApi']);
Route::post('/login', [AuthenticatedSessionController::class, 'storeApi']);

Route::middleware('auth:sanctum')->group(function () {
    Route::post('/logout', [AuthenticatedSessionController::class, 'destroyApi']);
    Route::get('/user', function (Request $request) {
        return response()->json($request->user());
    });
});


Route::apiResource('user', UserController::class)->except([
    'create', 'edit'
]);

Route::get('/userList/{userId}/lists', [UserListController::class, 'getByUser']);

Route::get('/user-lists/type/{listType}/{userId}', [UserListController::class, 'getByListType']);
Route::apiResource('userList', UserListController::class)->except([
    'create', 'edit'
]);

Route::get('/list-media/userList/{userListId}', [ListMediaController::class, 'getByUserListId']);
Route::apiResource('listMedia', ListMediaController::class)->except([
    'create', 'edit'
]);

Route::apiResource('rating', RatingController::class)->except([
    'create', 'edit'
]);