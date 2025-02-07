<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Rating;
use Illuminate\Http\Request;
use App\Http\Resources\RatingResource;
use App\Http\Requests\StoreRatingRequest;
use Illuminate\Support\Facades\Http;

class RatingController extends Controller
{
   public function index()
   {
      $ratings = Rating::all();
      return RatingResource::collection($ratings);
   }

   public function indexWeb(Request $request, $userId = null)
   {
       $query = Rating::query();
   
       // Filtro pelo id_user caso seja fornecido
       if ($userId) {
           $query->where('id_user', $userId);
       }
   
       // Paginação e carregamento de usuários
       $ratingsList = $query->with('user')->get();
   
       // Token para autenticação na API do TMDB
       $bearerToken = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyODVkMDg3ZmE2N2M1ZGQxMGRhZGYwYjU0YzllNWJiZiIsIm5iZiI6MTczMTU5MzYxOS40MDg3NTY3LCJzdWIiOiI2NzFhMWIzMjc2OTEwN2Q3N2I0Nzg5ZTIiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.AphmMyEeUGyMLUYur8S0kiwRTBxzYsnurfudZb6CGJY';
   
       // Iterar sobre cada avaliação e buscar os detalhes do filme/série
       foreach ($ratingsList as $rating) {
           $mediaType = null;
           $mediaTitle = 'Não encontrado';
   
           // Testar primeiro como 'movie'
           $response = Http::withHeaders([
               'Authorization' => $bearerToken,
           ])->get("https://api.themoviedb.org/3/movie/{$rating->id_media}", [
               'language' => 'pt-PT',
           ]);
   
           if ($response->successful() && $response->json()) {
               $mediaType = 'movie';
               $mediaTitle = $response->json('title') ?? 'Não encontrado';
           } else {
               // Se não for um 'movie', testar como 'tv'
               $response = Http::withHeaders([
                   'Authorization' => $bearerToken,
               ])->get("https://api.themoviedb.org/3/tv/{$rating->id_media}", [
                   'language' => 'pt-PT',
               ]);
   
               if ($response->successful() && $response->json()) {
                   $mediaType = 'tv';
                   $mediaTitle = $response->json('name') ?? 'Não encontrado';
               }
           }
   
           $rating->mediaType = $mediaType; // Adiciona dinamicamente o tipo
           $rating->media = $mediaTitle;    // Adiciona o nome do filme/série
       }
   
       // Filtro pelo tipo de mídia (TV ou Movie)
       $selectedType = $request->input('type');
       if ($selectedType && in_array($selectedType, ['movie', 'tv'])) {
           $ratingsList = $ratingsList->filter(function ($rating) use ($selectedType) {
               return $rating->mediaType === $selectedType;
           });
       }
   
       // Filtro pelo nome do usuário
       if ($request->has('user_name') && $request->user_name) {
           $userName = $request->user_name;
           $ratingsList = $ratingsList->filter(function ($rating) use ($userName) {
               return stripos($rating->user->name, $userName) !== false;
           });
       }
   
       // Paginação manual após filtragem
       $currentPage = \Illuminate\Pagination\LengthAwarePaginator::resolveCurrentPage();
       $perPage = 10;
       $paginatedRatings = new \Illuminate\Pagination\LengthAwarePaginator(
           $ratingsList->forPage($currentPage, $perPage),
           $ratingsList->count(),
           $perPage,
           $currentPage,
           ['path' => request()->url(), 'query' => request()->query()]
       );
   
       return view('ratings.indexWeb', [
           'ratingsList' => $paginatedRatings,
           'filters' => $request->all(),
           'userId' => $userId,
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
    public function store(StoreRatingRequest $request)
    {
       $rating = Rating::create($request->validated());
       return new RatingResource($rating);
    }
    
    /*
    * Display the specified resource.
    */
    public function show(string $id)
    {
       $rating = Rating::findOrFail($id);
       return new RatingResource($rating);
    }
    
    /*
    * Show the form for editing the specified resource.
    */
    public function edit(Rating $Rating)
    {
        //
    }
    
    /*
    * Update the specified resource in storage.
    */
    public function update(StoreRatingRequest $request, Rating $rating)
    {
       $rating->update($request->validated());
       return new RatingResource($rating);
    }
    
    /*
    * Remove the specified resource from storage.
    */
    public function destroy(Rating $rating)
    {
       $rating->delete();
       //return response(null, 204);
       // return new RatingResource(null);
       return new RatingResource($rating);
    }
    
    public function destroyWeb($id)
    {
        $rating = Rating::findOrFail($id);
        $rating->delete();

        return response()->json(['message' => 'Avaliação excluída com sucesso!'], 200);
    }

}
