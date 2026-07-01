package com.example.newsapp.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.newsapp.model.Noticia;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NoticiaDao_Impl implements NoticiaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Noticia> __insertionAdapterOfNoticia;

  private final EntityDeletionOrUpdateAdapter<Noticia> __deletionAdapterOfNoticia;

  private final EntityDeletionOrUpdateAdapter<Noticia> __updateAdapterOfNoticia;

  private final SharedSQLiteStatement __preparedStmtOfEliminarTodas;

  public NoticiaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNoticia = new EntityInsertionAdapter<Noticia>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `noticias` (`id`,`titulo`,`descricao`,`fonte`,`urlImagem`,`urlNoticia`,`dataPublicacao`,`categoria`,`totalFontes`,`favorita`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Noticia entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitulo());
        statement.bindString(3, entity.getDescricao());
        statement.bindString(4, entity.getFonte());
        statement.bindString(5, entity.getUrlImagem());
        statement.bindString(6, entity.getUrlNoticia());
        statement.bindString(7, entity.getDataPublicacao());
        statement.bindString(8, entity.getCategoria());
        statement.bindLong(9, entity.getTotalFontes());
        final int _tmp = entity.getFavorita() ? 1 : 0;
        statement.bindLong(10, _tmp);
      }
    };
    this.__deletionAdapterOfNoticia = new EntityDeletionOrUpdateAdapter<Noticia>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `noticias` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Noticia entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfNoticia = new EntityDeletionOrUpdateAdapter<Noticia>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `noticias` SET `id` = ?,`titulo` = ?,`descricao` = ?,`fonte` = ?,`urlImagem` = ?,`urlNoticia` = ?,`dataPublicacao` = ?,`categoria` = ?,`totalFontes` = ?,`favorita` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Noticia entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitulo());
        statement.bindString(3, entity.getDescricao());
        statement.bindString(4, entity.getFonte());
        statement.bindString(5, entity.getUrlImagem());
        statement.bindString(6, entity.getUrlNoticia());
        statement.bindString(7, entity.getDataPublicacao());
        statement.bindString(8, entity.getCategoria());
        statement.bindLong(9, entity.getTotalFontes());
        final int _tmp = entity.getFavorita() ? 1 : 0;
        statement.bindLong(10, _tmp);
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfEliminarTodas = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM noticias";
        return _query;
      }
    };
  }

  @Override
  public Object inserir(final Noticia noticia, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfNoticia.insert(noticia);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object eliminar(final Noticia noticia, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfNoticia.handle(noticia);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object atualizar(final Noticia noticia, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfNoticia.handle(noticia);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object eliminarTodas(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfEliminarTodas.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfEliminarTodas.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Noticia>> obterTodas() {
    final String _sql = "SELECT * FROM noticias ORDER BY dataPublicacao DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"noticias"}, new Callable<List<Noticia>>() {
      @Override
      @NonNull
      public List<Noticia> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfDescricao = CursorUtil.getColumnIndexOrThrow(_cursor, "descricao");
          final int _cursorIndexOfFonte = CursorUtil.getColumnIndexOrThrow(_cursor, "fonte");
          final int _cursorIndexOfUrlImagem = CursorUtil.getColumnIndexOrThrow(_cursor, "urlImagem");
          final int _cursorIndexOfUrlNoticia = CursorUtil.getColumnIndexOrThrow(_cursor, "urlNoticia");
          final int _cursorIndexOfDataPublicacao = CursorUtil.getColumnIndexOrThrow(_cursor, "dataPublicacao");
          final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
          final int _cursorIndexOfTotalFontes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFontes");
          final int _cursorIndexOfFavorita = CursorUtil.getColumnIndexOrThrow(_cursor, "favorita");
          final List<Noticia> _result = new ArrayList<Noticia>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Noticia _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitulo;
            _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            final String _tmpDescricao;
            _tmpDescricao = _cursor.getString(_cursorIndexOfDescricao);
            final String _tmpFonte;
            _tmpFonte = _cursor.getString(_cursorIndexOfFonte);
            final String _tmpUrlImagem;
            _tmpUrlImagem = _cursor.getString(_cursorIndexOfUrlImagem);
            final String _tmpUrlNoticia;
            _tmpUrlNoticia = _cursor.getString(_cursorIndexOfUrlNoticia);
            final String _tmpDataPublicacao;
            _tmpDataPublicacao = _cursor.getString(_cursorIndexOfDataPublicacao);
            final String _tmpCategoria;
            _tmpCategoria = _cursor.getString(_cursorIndexOfCategoria);
            final int _tmpTotalFontes;
            _tmpTotalFontes = _cursor.getInt(_cursorIndexOfTotalFontes);
            final boolean _tmpFavorita;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorita);
            _tmpFavorita = _tmp != 0;
            _item = new Noticia(_tmpId,_tmpTitulo,_tmpDescricao,_tmpFonte,_tmpUrlImagem,_tmpUrlNoticia,_tmpDataPublicacao,_tmpCategoria,_tmpTotalFontes,_tmpFavorita);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object obterPorId(final int id, final Continuation<? super Noticia> $completion) {
    final String _sql = "SELECT * FROM noticias WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Noticia>() {
      @Override
      @Nullable
      public Noticia call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfDescricao = CursorUtil.getColumnIndexOrThrow(_cursor, "descricao");
          final int _cursorIndexOfFonte = CursorUtil.getColumnIndexOrThrow(_cursor, "fonte");
          final int _cursorIndexOfUrlImagem = CursorUtil.getColumnIndexOrThrow(_cursor, "urlImagem");
          final int _cursorIndexOfUrlNoticia = CursorUtil.getColumnIndexOrThrow(_cursor, "urlNoticia");
          final int _cursorIndexOfDataPublicacao = CursorUtil.getColumnIndexOrThrow(_cursor, "dataPublicacao");
          final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
          final int _cursorIndexOfTotalFontes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFontes");
          final int _cursorIndexOfFavorita = CursorUtil.getColumnIndexOrThrow(_cursor, "favorita");
          final Noticia _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitulo;
            _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            final String _tmpDescricao;
            _tmpDescricao = _cursor.getString(_cursorIndexOfDescricao);
            final String _tmpFonte;
            _tmpFonte = _cursor.getString(_cursorIndexOfFonte);
            final String _tmpUrlImagem;
            _tmpUrlImagem = _cursor.getString(_cursorIndexOfUrlImagem);
            final String _tmpUrlNoticia;
            _tmpUrlNoticia = _cursor.getString(_cursorIndexOfUrlNoticia);
            final String _tmpDataPublicacao;
            _tmpDataPublicacao = _cursor.getString(_cursorIndexOfDataPublicacao);
            final String _tmpCategoria;
            _tmpCategoria = _cursor.getString(_cursorIndexOfCategoria);
            final int _tmpTotalFontes;
            _tmpTotalFontes = _cursor.getInt(_cursorIndexOfTotalFontes);
            final boolean _tmpFavorita;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorita);
            _tmpFavorita = _tmp != 0;
            _result = new Noticia(_tmpId,_tmpTitulo,_tmpDescricao,_tmpFonte,_tmpUrlImagem,_tmpUrlNoticia,_tmpDataPublicacao,_tmpCategoria,_tmpTotalFontes,_tmpFavorita);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Noticia>> pesquisar(final String texto) {
    final String _sql = "SELECT * FROM noticias WHERE titulo LIKE '%' || ? || '%' OR fonte LIKE '%' || ? || '%' ORDER BY dataPublicacao DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, texto);
    _argIndex = 2;
    _statement.bindString(_argIndex, texto);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"noticias"}, new Callable<List<Noticia>>() {
      @Override
      @NonNull
      public List<Noticia> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfDescricao = CursorUtil.getColumnIndexOrThrow(_cursor, "descricao");
          final int _cursorIndexOfFonte = CursorUtil.getColumnIndexOrThrow(_cursor, "fonte");
          final int _cursorIndexOfUrlImagem = CursorUtil.getColumnIndexOrThrow(_cursor, "urlImagem");
          final int _cursorIndexOfUrlNoticia = CursorUtil.getColumnIndexOrThrow(_cursor, "urlNoticia");
          final int _cursorIndexOfDataPublicacao = CursorUtil.getColumnIndexOrThrow(_cursor, "dataPublicacao");
          final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
          final int _cursorIndexOfTotalFontes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFontes");
          final int _cursorIndexOfFavorita = CursorUtil.getColumnIndexOrThrow(_cursor, "favorita");
          final List<Noticia> _result = new ArrayList<Noticia>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Noticia _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitulo;
            _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            final String _tmpDescricao;
            _tmpDescricao = _cursor.getString(_cursorIndexOfDescricao);
            final String _tmpFonte;
            _tmpFonte = _cursor.getString(_cursorIndexOfFonte);
            final String _tmpUrlImagem;
            _tmpUrlImagem = _cursor.getString(_cursorIndexOfUrlImagem);
            final String _tmpUrlNoticia;
            _tmpUrlNoticia = _cursor.getString(_cursorIndexOfUrlNoticia);
            final String _tmpDataPublicacao;
            _tmpDataPublicacao = _cursor.getString(_cursorIndexOfDataPublicacao);
            final String _tmpCategoria;
            _tmpCategoria = _cursor.getString(_cursorIndexOfCategoria);
            final int _tmpTotalFontes;
            _tmpTotalFontes = _cursor.getInt(_cursorIndexOfTotalFontes);
            final boolean _tmpFavorita;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorita);
            _tmpFavorita = _tmp != 0;
            _item = new Noticia(_tmpId,_tmpTitulo,_tmpDescricao,_tmpFonte,_tmpUrlImagem,_tmpUrlNoticia,_tmpDataPublicacao,_tmpCategoria,_tmpTotalFontes,_tmpFavorita);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Noticia>> obterPorCategoria(final String categoria) {
    final String _sql = "SELECT * FROM noticias WHERE categoria = ? ORDER BY dataPublicacao DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, categoria);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"noticias"}, new Callable<List<Noticia>>() {
      @Override
      @NonNull
      public List<Noticia> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfDescricao = CursorUtil.getColumnIndexOrThrow(_cursor, "descricao");
          final int _cursorIndexOfFonte = CursorUtil.getColumnIndexOrThrow(_cursor, "fonte");
          final int _cursorIndexOfUrlImagem = CursorUtil.getColumnIndexOrThrow(_cursor, "urlImagem");
          final int _cursorIndexOfUrlNoticia = CursorUtil.getColumnIndexOrThrow(_cursor, "urlNoticia");
          final int _cursorIndexOfDataPublicacao = CursorUtil.getColumnIndexOrThrow(_cursor, "dataPublicacao");
          final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
          final int _cursorIndexOfTotalFontes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFontes");
          final int _cursorIndexOfFavorita = CursorUtil.getColumnIndexOrThrow(_cursor, "favorita");
          final List<Noticia> _result = new ArrayList<Noticia>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Noticia _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitulo;
            _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            final String _tmpDescricao;
            _tmpDescricao = _cursor.getString(_cursorIndexOfDescricao);
            final String _tmpFonte;
            _tmpFonte = _cursor.getString(_cursorIndexOfFonte);
            final String _tmpUrlImagem;
            _tmpUrlImagem = _cursor.getString(_cursorIndexOfUrlImagem);
            final String _tmpUrlNoticia;
            _tmpUrlNoticia = _cursor.getString(_cursorIndexOfUrlNoticia);
            final String _tmpDataPublicacao;
            _tmpDataPublicacao = _cursor.getString(_cursorIndexOfDataPublicacao);
            final String _tmpCategoria;
            _tmpCategoria = _cursor.getString(_cursorIndexOfCategoria);
            final int _tmpTotalFontes;
            _tmpTotalFontes = _cursor.getInt(_cursorIndexOfTotalFontes);
            final boolean _tmpFavorita;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorita);
            _tmpFavorita = _tmp != 0;
            _item = new Noticia(_tmpId,_tmpTitulo,_tmpDescricao,_tmpFonte,_tmpUrlImagem,_tmpUrlNoticia,_tmpDataPublicacao,_tmpCategoria,_tmpTotalFontes,_tmpFavorita);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Noticia>> obterFavoritas() {
    final String _sql = "SELECT * FROM noticias WHERE favorita = 1 ORDER BY dataPublicacao DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"noticias"}, new Callable<List<Noticia>>() {
      @Override
      @NonNull
      public List<Noticia> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfDescricao = CursorUtil.getColumnIndexOrThrow(_cursor, "descricao");
          final int _cursorIndexOfFonte = CursorUtil.getColumnIndexOrThrow(_cursor, "fonte");
          final int _cursorIndexOfUrlImagem = CursorUtil.getColumnIndexOrThrow(_cursor, "urlImagem");
          final int _cursorIndexOfUrlNoticia = CursorUtil.getColumnIndexOrThrow(_cursor, "urlNoticia");
          final int _cursorIndexOfDataPublicacao = CursorUtil.getColumnIndexOrThrow(_cursor, "dataPublicacao");
          final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
          final int _cursorIndexOfTotalFontes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFontes");
          final int _cursorIndexOfFavorita = CursorUtil.getColumnIndexOrThrow(_cursor, "favorita");
          final List<Noticia> _result = new ArrayList<Noticia>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Noticia _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitulo;
            _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            final String _tmpDescricao;
            _tmpDescricao = _cursor.getString(_cursorIndexOfDescricao);
            final String _tmpFonte;
            _tmpFonte = _cursor.getString(_cursorIndexOfFonte);
            final String _tmpUrlImagem;
            _tmpUrlImagem = _cursor.getString(_cursorIndexOfUrlImagem);
            final String _tmpUrlNoticia;
            _tmpUrlNoticia = _cursor.getString(_cursorIndexOfUrlNoticia);
            final String _tmpDataPublicacao;
            _tmpDataPublicacao = _cursor.getString(_cursorIndexOfDataPublicacao);
            final String _tmpCategoria;
            _tmpCategoria = _cursor.getString(_cursorIndexOfCategoria);
            final int _tmpTotalFontes;
            _tmpTotalFontes = _cursor.getInt(_cursorIndexOfTotalFontes);
            final boolean _tmpFavorita;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorita);
            _tmpFavorita = _tmp != 0;
            _item = new Noticia(_tmpId,_tmpTitulo,_tmpDescricao,_tmpFonte,_tmpUrlImagem,_tmpUrlNoticia,_tmpDataPublicacao,_tmpCategoria,_tmpTotalFontes,_tmpFavorita);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Noticia>> obterOrdenadasPorTitulo() {
    final String _sql = "SELECT * FROM noticias ORDER BY titulo ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"noticias"}, new Callable<List<Noticia>>() {
      @Override
      @NonNull
      public List<Noticia> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfDescricao = CursorUtil.getColumnIndexOrThrow(_cursor, "descricao");
          final int _cursorIndexOfFonte = CursorUtil.getColumnIndexOrThrow(_cursor, "fonte");
          final int _cursorIndexOfUrlImagem = CursorUtil.getColumnIndexOrThrow(_cursor, "urlImagem");
          final int _cursorIndexOfUrlNoticia = CursorUtil.getColumnIndexOrThrow(_cursor, "urlNoticia");
          final int _cursorIndexOfDataPublicacao = CursorUtil.getColumnIndexOrThrow(_cursor, "dataPublicacao");
          final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
          final int _cursorIndexOfTotalFontes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFontes");
          final int _cursorIndexOfFavorita = CursorUtil.getColumnIndexOrThrow(_cursor, "favorita");
          final List<Noticia> _result = new ArrayList<Noticia>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Noticia _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitulo;
            _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            final String _tmpDescricao;
            _tmpDescricao = _cursor.getString(_cursorIndexOfDescricao);
            final String _tmpFonte;
            _tmpFonte = _cursor.getString(_cursorIndexOfFonte);
            final String _tmpUrlImagem;
            _tmpUrlImagem = _cursor.getString(_cursorIndexOfUrlImagem);
            final String _tmpUrlNoticia;
            _tmpUrlNoticia = _cursor.getString(_cursorIndexOfUrlNoticia);
            final String _tmpDataPublicacao;
            _tmpDataPublicacao = _cursor.getString(_cursorIndexOfDataPublicacao);
            final String _tmpCategoria;
            _tmpCategoria = _cursor.getString(_cursorIndexOfCategoria);
            final int _tmpTotalFontes;
            _tmpTotalFontes = _cursor.getInt(_cursorIndexOfTotalFontes);
            final boolean _tmpFavorita;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorita);
            _tmpFavorita = _tmp != 0;
            _item = new Noticia(_tmpId,_tmpTitulo,_tmpDescricao,_tmpFonte,_tmpUrlImagem,_tmpUrlNoticia,_tmpDataPublicacao,_tmpCategoria,_tmpTotalFontes,_tmpFavorita);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Noticia>> obterOrdenadasPorFontes() {
    final String _sql = "SELECT * FROM noticias ORDER BY totalFontes DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"noticias"}, new Callable<List<Noticia>>() {
      @Override
      @NonNull
      public List<Noticia> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfDescricao = CursorUtil.getColumnIndexOrThrow(_cursor, "descricao");
          final int _cursorIndexOfFonte = CursorUtil.getColumnIndexOrThrow(_cursor, "fonte");
          final int _cursorIndexOfUrlImagem = CursorUtil.getColumnIndexOrThrow(_cursor, "urlImagem");
          final int _cursorIndexOfUrlNoticia = CursorUtil.getColumnIndexOrThrow(_cursor, "urlNoticia");
          final int _cursorIndexOfDataPublicacao = CursorUtil.getColumnIndexOrThrow(_cursor, "dataPublicacao");
          final int _cursorIndexOfCategoria = CursorUtil.getColumnIndexOrThrow(_cursor, "categoria");
          final int _cursorIndexOfTotalFontes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFontes");
          final int _cursorIndexOfFavorita = CursorUtil.getColumnIndexOrThrow(_cursor, "favorita");
          final List<Noticia> _result = new ArrayList<Noticia>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Noticia _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitulo;
            _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            final String _tmpDescricao;
            _tmpDescricao = _cursor.getString(_cursorIndexOfDescricao);
            final String _tmpFonte;
            _tmpFonte = _cursor.getString(_cursorIndexOfFonte);
            final String _tmpUrlImagem;
            _tmpUrlImagem = _cursor.getString(_cursorIndexOfUrlImagem);
            final String _tmpUrlNoticia;
            _tmpUrlNoticia = _cursor.getString(_cursorIndexOfUrlNoticia);
            final String _tmpDataPublicacao;
            _tmpDataPublicacao = _cursor.getString(_cursorIndexOfDataPublicacao);
            final String _tmpCategoria;
            _tmpCategoria = _cursor.getString(_cursorIndexOfCategoria);
            final int _tmpTotalFontes;
            _tmpTotalFontes = _cursor.getInt(_cursorIndexOfTotalFontes);
            final boolean _tmpFavorita;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfFavorita);
            _tmpFavorita = _tmp != 0;
            _item = new Noticia(_tmpId,_tmpTitulo,_tmpDescricao,_tmpFonte,_tmpUrlImagem,_tmpUrlNoticia,_tmpDataPublicacao,_tmpCategoria,_tmpTotalFontes,_tmpFavorita);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object contar(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM noticias";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
