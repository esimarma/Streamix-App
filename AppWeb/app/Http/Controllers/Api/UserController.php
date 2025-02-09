<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\SearchRequest;
use App\Http\Resources\UserResource;
use App\Models\User;
use App\Models\UserList;

class UserController extends Controller
{
    /*
   * Display a listing of the resource.
   */
  public function index()
  {
     $user = User::all();
     return UserResource::collection($user);
  }

  public function indexWeb(SearchRequest $request)
  {
      $query = User::query();
  
      // Verifica se há um termo de pesquisa
      if ($request->has('search') && $request->search) {
          $search = $request->search;
          $query->where('name', 'LIKE', "%{$search}%")
                ->orWhere('email', 'LIKE', "%{$search}%");
      }
  
      $users = $query->paginate(10); // Paginação opcional
  
      return view('users.indexWeb', compact('users'));
  }

   /*
   * Display the specified resource.
   */
   public function show(int $id)
   {
      $users = User::findOrFail($id);
      return new UserResource($users);
   }

    // Método para atualizar o tempo de filme assistido
    public function updateUserMovieWastedTime($userId, $runtime)
    {
      $user = User::findOrFail($userId);

      if ($user) {
         $user->movie_wasted_time_min += $runtime;
         $user->save();
 
         return response()->json([
               'message' => 'Tempo de filme atualizado com sucesso!',
               'data' => $user,
         ], 200);
      }

        return response()->json([
            'message' => 'Utilizador não encontrado.',
        ], 404);
    }
}
