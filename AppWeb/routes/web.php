<?php

use App\Http\Controllers\Api\ListMediaController;
use App\Http\Controllers\Api\RatingController;
use App\Http\Controllers\Api\UserController;
use App\Http\Controllers\Api\UserListController;
use App\Http\Controllers\ProfileController;
use Illuminate\Support\Facades\Route;

Route::get('/', function () {
    return view('welcome');
});

Route::get('/dashboard', function () {
    return view('dashboard');
})->middleware(['auth', 'verified'])->name('dashboard');

Route::middleware('auth')->group(function () {
    Route::get('/profile', [ProfileController::class, 'edit'])->name('profile.edit');
    Route::patch('/profile', [ProfileController::class, 'update'])->name('profile.update');
    Route::delete('/profile', [ProfileController::class, 'destroy'])->name('profile.destroy');
});

Route::get('/users', [UserController::class, 'indexWeb'])->name('users.indexWeb');

Route::get('/userList', [UserListController::class, 'indexWeb'])->name('user-lists.indexWeb');
Route::get('/userList/user/{id}', [UserListController::class, 'getByUser'])->name('user-lists.lists');
Route::put('/user-lists/{id}', [UserListController::class, 'updateWeb'])->name('user-lists.update');
Route::delete('/user-lists/{id}', [UserListController::class, 'destroyWeb'])->name('user-lists.destroy');

Route::get('/list-media', [ListMediaController::class, 'indexWeb'])->name('list-media.indexWeb');

Route::resource('ratings', RatingController::class);

require __DIR__.'/auth.php';
