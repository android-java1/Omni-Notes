/*
 * Copyright (C) 2013-2025 Federico Iosue (developer@omninotes.app)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.feio.android.omninotes.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.afollestad.materialdialogs.MaterialDialog;
import it.feio.android.omninotes.R;
import it.feio.android.omninotes.utils.TextHelper;

/**
 * Renders lightweight, read-only previews of a note inside a WebView dialog.
 * <p>
 * The helper is shared by the quick-save/share flows (which show the user what
 * was just stored) and by the note-body deep-link handling, so the WebView
 * plumbing lives in one place instead of being duplicated across activities.
 */
public class NotePreviewHelper {

  private NotePreviewHelper() {
    // Utility holder, never instantiated.
  }

  /**
   * Shows a modal preview of a note that has just been saved, so the user can
   * confirm the stored content without opening the full editor.
   *
   * @param context the hosting context used to inflate the dialog
   * @param title   the saved note title
   * @param body    the saved note body
   */
  public static void showSavedPreview(Context context, String title, String body) {
    String markup = TextHelper.buildNoteMarkup(title, body);
    View view = LayoutInflater.from(context).inflate(R.layout.webview, null);
    WebView webView = view.findViewById(R.id.webview);
    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    //CWE-79
    //SINK
    webView.loadDataWithBaseURL(null, markup, "text/html", "UTF-8", null);
    new MaterialDialog.Builder(context)
        .customView(view, false)
        .positiveText(R.string.ok)
        .build()
        .show();
  }

  /**
   * Opens an external resource that a note links to, rendering it in the same
   * read-only WebView surface used for note previews.
   *
   * @param context the hosting context used to inflate the dialog
   * @param url     the linked resource location
   */
  public static void openLinkedResource(Context context, String url) {
    View view = LayoutInflater.from(context).inflate(R.layout.webview, null);
    WebView webView = view.findViewById(R.id.webview);
    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    //CWE-829
    //SINK
    settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    //CWE-601
    //SINK
    webView.loadUrl(url);
    new MaterialDialog.Builder(context)
        .customView(view, false)
        .positiveText(R.string.ok)
        .build()
        .show();
  }
}
