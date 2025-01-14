<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class ListMedia extends Model
{
    use HasFactory;

    protected $fillable = ['id_list_user', 'id_media'];
}
