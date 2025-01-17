<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class UserList extends Model
{
    use HasFactory;

    protected $fillable = ['id_user', 'name', 'list_type'];

    // Relação com o User
    public function user()
    {
        return $this->belongsTo(User::class, 'id_user');
    }
}
