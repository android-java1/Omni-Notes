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

package it.feio.android.omninotes.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import androidx.annotation.Nullable;
import java.util.List;


public class IntentChecker {

  private IntentChecker() {
    // hides public constructor
  }

  /**
   * Retrieves
   */
  public static String resolveActivityPackage(Context ctx, Intent intent) {
    ComponentName activity = intent.resolveActivity(ctx.getPackageManager());
    return activity != null ? activity.getPackageName() : "";
  }

  /**
   * Checks intent and optionally features availability
   */
  public static boolean isAvailable(Context ctx, Intent intent, @Nullable String[] features) {
    boolean res = !getCompatiblePackages(ctx, intent).isEmpty();

    if (features != null) {
      for (String feature : features) {
        res = res && ctx.getPackageManager().hasSystemFeature(feature);
      }
    }
    return res;
  }

  /**
   * Returns the launcher shortcut's target when a compatible activity is present on the
   * device, so the caller can forward it straight to the launcher; otherwise null.
   */
  public static Intent resolveShortcutTarget(Context ctx, Intent candidate) {
    if (candidate != null && isAvailable(ctx, candidate, null)) {
      return candidate;
    }
    return null;
  }

  /**
   * Checks Intent's action
   *
   * @param i      Intent to ckeck
   * @param action Action to compare with
   */
  public static boolean checkAction(Intent i, String action) {
    return action.equals(i.getAction());
  }


  /**
   * Checks Intent's actions
   *
   * @param i       Intent to ckeck
   * @param actions Multiple actions to compare with
   */
  public static boolean checkAction(Intent i, String... actions) {
    for (String action : actions) {
      if (checkAction(i, action)) {
        return true;
      }
    }
    return false;
  }

  private static List<ResolveInfo> getCompatiblePackages(Context ctx, Intent intent) {
    final PackageManager mgr = ctx.getPackageManager();
    return mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
  }
}
