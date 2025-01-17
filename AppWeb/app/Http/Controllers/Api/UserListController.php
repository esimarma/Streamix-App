<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\StoreUserListRequest;
use App\Http\Requests\SearchRequest;
use Illuminate\Http\Request;
use App\Http\Resources\UserListResource;
use App\Models\User;
use App\Models\UserList;

class UserListController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index(Request $request)
    {
        $query = UserList::query();
    
        // Filtros de pesquisa
        if ($request->has('list_name') && $request->list_name) {
            $listName = $request->list_name;
            $query->where('name', 'LIKE', "%{$listName}%");
        }
    
        if ($request->has('user_name') && $request->user_name) {
            $userName = $request->user_name;
            $query->whereHas('user', function ($q) use ($userName) {
                $q->where('name', 'LIKE', "%{$userName}%");
            });
        }
    
        // Eager loading e paginação
        $userList = $query->with('user')->paginate(10);
    
        // Certifique-se de que está passando a variável correta
        return view('user-lists.index', compact('userList')); 
    }

    public function getByUser($userId)
    {
        // Obtemos o usuário (caso necessário para exibir detalhes na página)
        $user = User::findOrFail($userId);

        // Obtemos as listas associadas ao usuário
        $userList = UserList::where('id_user', $userId)->with('user')->paginate(10);

        // Retornamos a view com as listas e o usuário
        return view('user-lists.index', compact('userList', 'user'));
    }
    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(StoreUserListRequest $request)
    {
        $userList = UserList::create($request->validated());
        return new UserListResource($userList);
    }

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        $userList = UserList::findOrFail($id);
        return new UserListResource($userList);
    }

    /**
     * Show the form for editing the specified resource.
     */
    public function edit(UserList $userList)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(StoreUserListRequest $request, UserList $userList)
    {
        $userList->update($request->validated());
        return new UserListResource($userList);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(UserList $userList)
    {
        $userList->delete();
        return new UserListResource($userList);
    }
}
