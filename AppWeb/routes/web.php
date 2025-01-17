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


Route::get('/users', [UserController::class, 'index'])->name('users.index');
Route::get('/userList', [UserListController::class, 'index'])->name('user-lists.index');
Route::get('/userList/user/{id}', [UserListController::class, 'getByUser'])->name('user-lists.lists');

Route::resource('user-lists', UserListController::class);
Route::resource('list-media', ListMediaController::class);
Route::resource('ratings', RatingController::class);

require __DIR__.'/auth.php';
