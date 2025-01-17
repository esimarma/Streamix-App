<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\SearchRequest;
use App\Http\Resources\UserResource;
use App\Models\User;

class UserController extends Controller
{
    /*
   * Display a listing of the resource.
   */
  public function index(SearchRequest $request)
  {
      $query = User::query();
  
      // Verifica se há um termo de pesquisa
      if ($request->has('search') && $request->search) {
          $search = $request->search;
          $query->where('name', 'LIKE', "%{$search}%")
                ->orWhere('email', 'LIKE', "%{$search}%");
      }
  
      $users = $query->paginate(10); // Paginação opcional
  
      return view('users.index', compact('users'));
  }

   /*
   * Display the specified resource.
   */
   public function show(string $id)
   {
      $users = User::findOrFail($id);
      return new UserResource($users);
   }
}
