<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\SearchRequest;
use App\Models\ListMedia;
use App\Http\Resources\ListMediaResource;
use App\Http\Requests\StoreListMediaRequest;
use Illuminate\Support\Facades\Http;

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

   public function indexWeb(SearchRequest $request)
   {
       $query = ListMedia::query();
   
       // Paginação
       $listMedias = $query->paginate(10);
   
       // Token para autenticação
       $bearerToken = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyODVkMDg3ZmE2N2M1ZGQxMGRhZGYwYjU0YzllNWJiZiIsIm5iZiI6MTczMTU5MzYxOS40MDg3NTY3LCJzdWIiOiI2NzFhMWIzMjc2OTEwN2Q3N2I0Nzg5ZTIiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.AphmMyEeUGyMLUYur8S0kiwRTBxzYsnurfudZb6CGJY';
   
       // Itera sobre as mídias e busca os detalhes na API externa
       foreach ($listMedias as $media) {
           $endpoint = $media->media_type === 'tv' ? 'tv' : 'movie';
   
           $response = Http::withHeaders([
               'Authorization' => $bearerToken,
           ])->get("https://api.themoviedb.org/3/{$endpoint}/{$media->id_media}", [
               'language' => 'pt-BR', // Idioma opcional
           ]);
   
           if ($response->ok()) {
               $media->name = $response->json('title') ?? $response->json('name'); // 'title' para filmes, 'name' para séries
           } else {
               $media->name = 'Não encontrado';
           }
       }
   
       return view('list-media.indexWeb', compact('listMedias'));
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
      $validated = $request->validate([
         'id_list_user' => 'required|integer',
         'id_media' => 'required|string',
   ]);

   $listMedia = ListMedia::create($validated);

   return response()->json([
         'message' => 'Mídia criada com sucesso!',
         'data' => $listMedia,
   ], 201);
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
         $listMedia->delete();
         return response()->json([
            'message' => 'Registro excluído com sucesso!',
            'data' => null,
         ], 200);
   }
}
