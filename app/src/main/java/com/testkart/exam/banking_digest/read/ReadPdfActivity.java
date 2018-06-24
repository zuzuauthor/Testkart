package com.testkart.exam.banking_digest.read;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.link.DefaultLinkHandler;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;

import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.testkart.exam.R;
import com.testkart.exam.banking_digest.my_magazines.MySubscriptionsFragment;
import com.testkart.exam.helper.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by elfemo on 13/2/18.
 */

public class ReadPdfActivity extends BaseActivity {

    private PDFView pdfView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_read_pdf);

        //readFIleFromAsset();

        initViews();
    }

    private void initViews() {

        String fileName = getIntent().getStringExtra(MySubscriptionsFragment.KEY_INTENT_FILE_NAME);
        File pdfFile = new File(getIntent().getStringExtra(MySubscriptionsFragment.KEY_INTENT_FILE_PATH)+"/"+fileName);

        pdfView = (PDFView) findViewById(R.id.pdfView);

      //  pdfView.fromUri(uri)

        pdfView.fromFile(pdfFile)

        //pdfView.fromBytes(byte[])

        //pdfView.fromStream(inputStream) // stream is written to bytearray - native code cannot use Java Streams

        //pdfView.fromSource(DocumentSource)

       // pdfView.fromAsset("mozilla.pdf")
               // .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                // allows to draw something on the current page, usually visible in the middle of the screen
                .onDraw(new OnDrawListener() {
                    @Override
                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {


                    }
                })

                // allows to draw something on all pages, separately for every page. Called only for visible pages
                .onDrawAll(new OnDrawListener() {
                    @Override
                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {

                    }
                })

                // called after document is loaded and starts to be rendered
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {

                    }
                })


                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {

                    }
                })

                .onPageScroll(new OnPageScrollListener() {
                    @Override
                    public void onPageScrolled(int page, float positionOffset) {

                    }
                })

                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {

                    }
                })

                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {

                    }
                })

                // called after document is rendered for the first time
                .onRender(new OnRenderListener() {
                    @Override
                    public void onInitiallyRendered(int nbPages) {

                    }
                })


                // called on single tap, return true if handled, false to toggle scroll handle visibility
                .onTap(new OnTapListener() {
                    @Override
                    public boolean onTap(MotionEvent e) {



                        return false;
                    }
                })


                .enableAnnotationRendering(true) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(new DefaultScrollHandle(this))
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
               /// .linkHandler(new DefaultLinkHandler(this))
                .pageFitPolicy(FitPolicy.WIDTH)
                .load();
    }



}
