package com.example.bookstoremb.view;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Created by The eXo Platform SAS
 * Author : BinhNV
 *          binhnv@exoplatform.com
 * Jul 31, 2012  
 */
public class LineTextView extends TextView {

  private Rect rect;
  private Paint paint;
  
  /**
   * @param context
   */
  public LineTextView(Context context) {
    super(context);
  }

  /**
   * @param context
   * @param attrs
   */
  public LineTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    rect = new Rect();
    paint = new Paint();
    paint.setStyle(Paint.Style.STROKE);
    paint.setColor(0x800000FF);
  }

  /**
   * @param context
   * @param attrs
   * @param defStyle
   */
  public LineTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  /* (non-Javadoc)
   * @see android.widget.TextView#onDraw(android.graphics.Canvas)
   */
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    
    int height = getHeight();
    int lineheight = getLineHeight();
    int count = height / lineheight;
    if (getLineCount() > count) {
      count = getLineCount();
    }
    Rect r = rect;
    Paint p = paint;
    int baseline = getLineBounds(0, r);
    for (int i = 0; i < count; i++) {
      canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, p);
      baseline+=getLineHeight();
    }
  }

}
