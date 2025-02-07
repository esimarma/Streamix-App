<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\StoreUserListRequest;
use Illuminate\Http\Request;
use App\Http\Resources\UserListResource;
use App\Models\User;
use App\Models\UserList;

class UserListController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
       $userList = UserList::all();
       return UserListResource::collection($userList);
    }
    
    public function indexWeb(Request $request, $userId = null )
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

        // Filtro pelo id_list_user caso seja fornecido
        if ($userId) {
            $query->where('id_user', $userId);
        }
    
        // Eager loading e paginação
        $userList = $query->with('user')->paginate(10);
    
        // Certifique-se de que está passando a variável correta
        return view('user-lists.indexWeb', [
            'userList' => $userList,
            'filters' => $request->all(), // Passa todos os filtros para a view
            'userId' => $userId, // Passa o userListId para a view
        ]);
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

    public function updateWeb(Request $request, $id)
    {
        // Validação dos dados recebidos
        $validated = $request->validate([
            'nome' => 'required|string|max:255',
            'tipo' => 'required|string|max:255',
        ]);
    
        // Busca o registro pelo ID e atualiza os campos
        $userList = UserList::findOrFail($id);
        $userList->name = $validated['nome'];
        $userList->list_type = $validated['tipo'];
        $userList->save();
    
        // Retorna uma resposta de sucesso
        return response()->json(['message' => 'Lista atualizada com sucesso!'], 200);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(UserList $userList)
    {
        $userList->delete();
        return new UserListResource($userList);
    }

    public function destroyWeb($id)
    {
        $userList = UserList::findOrFail($id);
        $userList->delete();

        return response()->json(['message' => 'Lista excluída com sucesso!'], 200);
    }
}
