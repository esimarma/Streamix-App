<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up()
    {
        Schema::table('list_media', function (Blueprint $table) {
            $table->unsignedBigInteger('id_list_user')->change();
            $table->integer('id_media')->change();
        });
    }

    public function down()
    {
        Schema::table('list_media', function (Blueprint $table) {
            $table->string('id_list_user', 255)->change();
            $table->string('id_media', 255)->change();
        });
    }
};
