<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\SearchRequest;
use App\Models\ListMedia;
use App\Models\UserList;
use App\Http\Resources\ListMediaResource;
use App\Http\Requests\StoreListMediaRequest;
use Illuminate\Support\Facades\Http;
use Illuminate\Http\Request;

class ListMediaController extends Controller
{
    /*
   * Display a listing of the resource.
   */
   public function index()
   {
      $listMedias = ListMedia::all();
      return ListMediaResource::collection($listMedias);
   }

   public function indexWeb(Request $request, $userListId = null)
   {
       $query = ListMedia::query();
   
       // Filtro pelo nome da lista
       if ($request->has('list_name') && $request->list_name) {
           $query->whereHas('userList', function ($q) use ($request) {
               $q->where('name', 'LIKE', "%{$request->list_name}%");
           });
       }
   
       // Filtro pelo nome do usuário
       if ($request->has('user_name') && $request->user_name) {
           $query->whereHas('userList.user', function ($q) use ($request) {
               $q->where('name', 'LIKE', "%{$request->user_name}%");
           });
       }
   
       // Filtro por tipo (TV ou Filme)
       if ($request->has('type')) {
           if ($request->type === 'todos') {
               // Não filtra por tipo se for 'todos'
           } elseif (in_array($request->type, ['tv', 'movie'])) {
               $query->where('media_type', $request->type);
           }
       }
   
       // Filtro pelo id_list_user caso seja fornecido
       if ($userListId) {
           $query->where('id_list_user', $userListId);
       }
   
       // Paginação
       $listMedias = $query->paginate(10);
   
       // Token para autenticação
       $bearerToken = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyODVkMDg3ZmE2N2M1ZGQxMGRhZGYwYjU0YzllNWJiZiIsIm5iZiI6MTczMTU5MzYxOS40MDg3NTY3LCJzdWIiOiI2NzFhMWIzMjc2OTEwN2Q3N2I0Nzg5ZTIiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.AphmMyEeUGyMLUYur8S0kiwRTBxzYsnurfudZb6CGJY';
   
       // Itera sobre as mídias e busca os detalhes na API externa
       foreach ($listMedias as $media) {
           $response = Http::withHeaders([
               'Authorization' => $bearerToken,
           ])->get("https://api.themoviedb.org/3/{$media->media_type}/{$media->id_media}", [
               'language' => 'pt-PT',
           ]);
   
           if ($response->successful() && $response->json()) {
               $media->name = $response->json('title') ?? $response->json('name');
               if ($media->media_type === 'tv') {
                   $media->number_of_episodes = $response->json('number_of_episodes') ?? $response->json('episode_run_time');
                   $media->runtime = "N/A";
               } else {
                   $media->number_of_episodes = "N/A";
                   $media->runtime = $response->json('runtime') ?? $response->json('episode_run_time');
               }
           } else {
               $media->name = 'Não encontrado';
               $media->number_of_episodes = 'N/A';
               $media->runtime = 'N/A';
           }
   
           // Associa cada mídia a sua respectiva lista de usuário
           $media->userList = UserList::with('user')->find($media->id_list_user);
       }
   
       // Passa os filtros e dados para a view
       return view('list-media.indexWeb', [
           'listMedias' => $listMedias,
           'filters' => $request->all(), // Passa todos os filtros para a view
           'userListId' => $userListId, // Passa o userListId para a view
       ]);
   }

   /*
   * Show the form for creating a new resource.
   */
   public function create()
   {
      //
   }

   /*
   * Store a newly created resource in storage.
   */
  public function store(StoreListMediaRequest $request)
  {
      $listMedia = ListMedia::create([
          'id_list_user' => $request->id_list_user,
          'id_media' => $request->id_media,
          'media_type' => $request->media_type,
      ]);
  
      // Token para autenticação
      $bearerToken = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyODVkMDg3ZmE2N2M1ZGQxMGRhZGYwYjU0YzllNWJiZiIsIm5iZiI6MTczMTU5MzYxOS40MDg3NTY3LCJzdWIiOiI2NzFhMWIzMjc2OTEwN2Q3N2I0Nzg5ZTIiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.AphmMyEeUGyMLUYur8S0kiwRTBxzYsnurfudZb6CGJY';
  
      $response = Http::withHeaders([
          'Authorization' => $bearerToken,
      ])->get("https://api.themoviedb.org/3/{$listMedia->media_type}/{$listMedia->id_media}", [
          'language' => 'pt-PT',
      ]);
  
      $userList = UserList::findOrFail($request->id_list_user);
      if ($userList) {
          // Verificação se o tipo de mídia é 'movie' e o tipo de lista é 'visto'
         if ($response->ok()) {
            $runtime = $response->json('runtime') ?? ($response->json('episode_run_time')[0] ?? 0);
   
            if ($listMedia->media_type === 'movie' && $userList->list_type === 'visto') {
               $userController = new UserController();
               $userController->updateUserMovieWastedTime($userList->id_user, $runtime);
            }
      }
      }
      
  
      if ($listMedia) {
          return response()->json(['message' => 'Salvo com sucesso!', 'data' => $listMedia], 201);
      } else {
          return response()->json(['message' => 'Erro ao salvar'], 500);
      }
  }
   /*
   * Display the specified resource.
   */
   public function show(string $id)
   {
      $listMedias = ListMedia::findOrFail($id);
      return new ListMediaResource($listMedias);
   }

   /*
   * Show the form for editing the specified resource.
   */
   public function edit(ListMedia $listMedias)
   {
      //
   }

   /*
   * Update the specified resource in storage.
   */
   public function update(StoreListMediaRequest $request, ListMedia $listMedia)
   {
      $listMedia->update($request->validated());

      return new ListMediaResource($listMedia);
   }

   /*
   * Remove the specified resource from storage.
   */
  public function destroy(ListMedia $listMedia)
   {
      // Token para autenticação na API externa
      $bearerToken = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyODVkMDg3ZmE2N2M1ZGQxMGRhZGYwYjU0YzllNWJiZiIsIm5iZiI6MTczMTU5MzYxOS40MDg3NTY3LCJzdWIiOiI2NzFhMWIzMjc2OTEwN2Q3N2I0Nzg5ZTIiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.AphmMyEeUGyMLUYur8S0kiwRTBxzYsnurfudZb6CGJY';

      // Obtemos os detalhes do filme/série
      $response = Http::withHeaders([
         'Authorization' => $bearerToken,
      ])->get("https://api.themoviedb.org/3/{$listMedia->media_type}/{$listMedia->id_media}", [
         'language' => 'pt-PT',
      ]);

      if ($response->ok()) {
         $runtime = $response->json('runtime') ?? ($response->json('episode_run_time')[0] ?? 0);
         
         // Obtém a lista do usuário
         $userList = UserList::findOrFail($listMedia->id_list_user);

         if ($listMedia->media_type === 'movie' && $userList->list_type === 'visto') {
               // Atualiza o tempo gasto pelo usuário (reduzindo)
               $userController = new UserController();
               $userController->updateUserMovieWastedTime($userList->id_user, -$runtime);
         }
      }

      // Exclui o ListMedia
      $listMedia->delete();

      return response()->json(['message' => 'Mídia removida e tempo atualizado com sucesso!'], 200);
   }

   public function destroyWeb($id)
   {
      $listMedia = ListMedia::findOrFail($id);

      // Token para autenticação na API externa
      $bearerToken = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyODVkMDg3ZmE2N2M1ZGQxMGRhZGYwYjU0YzllNWJiZiIsIm5iZiI6MTczMTU5MzYxOS40MDg3NTY3LCJzdWIiOiI2NzFhMWIzMjc2OTEwN2Q3N2I0Nzg5ZTIiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.AphmMyEeUGyMLUYur8S0kiwRTBxzYsnurfudZb6CGJY';

      // Obtemos os detalhes do filme/série
      $response = Http::withHeaders([
         'Authorization' => $bearerToken,
      ])->get("https://api.themoviedb.org/3/{$listMedia->media_type}/{$listMedia->id_media}", [
         'language' => 'pt-PT',
      ]);

      if ($response->ok()) {
         $runtime = $response->json('runtime') ?? ($response->json('episode_run_time')[0] ?? 0);
         
         // Obtém a lista do usuário
         $userList = UserList::findOrFail($listMedia->id_list_user);

         if ($listMedia->media_type === 'movie' && $userList->list_type === 'visto') {
               // Atualiza o tempo gasto pelo usuário (reduzindo)
               $userController = new UserController();
               $userController->updateUserMovieWastedTime($userList->id_user, -$runtime);
         }
      }

      // Exclui o ListMedia
      $listMedia->delete();

      return response()->json(['message' => 'Mídia removida e tempo atualizado com sucesso!'], 200);
   }
}
