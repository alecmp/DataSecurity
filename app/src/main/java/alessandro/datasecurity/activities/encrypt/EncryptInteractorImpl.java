package alessandro.datasecurity.activities.encrypt;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import alessandro.datasecurity.algorithms.Embedding;

class EncryptInteractorImpl implements EncryptInteractor {

  private EncryptInteractorListener listener;

  EncryptInteractorImpl(EncryptInteractorListener listener) {
    this.listener = listener;
  }

  @Override
  public void performSteganography(String message, Bitmap coverImage, Bitmap secretImage, String key) {
    if (secretImage == null && message != null) {
      new EmbedSecretMessage(message, coverImage, null, key).execute();
    } else if(secretImage != null){
      new EmbedSecretMessage(null, coverImage, secretImage, null).execute();
    } else {
      listener.onPerformSteganographyFailure();
    }
  }

  /**
   * AsyncTask which embeds secret message in the background not to block main frame
   */
  private class EmbedSecretMessage extends AsyncTask<Void, Void, Bitmap> {
    String message;
    Bitmap coverImage, secretImage;
    String key;

    EmbedSecretMessage(String message, Bitmap coverImage, Bitmap secretImage, String key) {
      this.message = message;
      this.coverImage = coverImage;
      this.secretImage = secretImage;
      this.key = key;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
      Bitmap stegoImage = null;

      if (message != null && secretImage == null && coverImage != null) {
        stegoImage = Embedding.embedSecretText(this.message, this.coverImage, this.key);
      } else if (message == null && secretImage != null && coverImage != null) {
        stegoImage = Embedding.embedSecretImage(this.coverImage, this.secretImage);
      }

      return stegoImage;
    }

    @Override
    protected void onPostExecute(Bitmap stegoImage) {
      if (stegoImage != null) {
        listener.onPerformSteganographySuccessful(stegoImage);
      } else {
        listener.onPerformSteganographyFailure();
      }
    }
  }

  interface EncryptInteractorListener {

    /**
     * Listener which is invoked after successfully performed steganography
     * @param stegoImage Bitmap which is the result image of steganography
     */
    void onPerformSteganographySuccessful(Bitmap stegoImage);

    /**
     * Listener which is invoked after failure during steganography
     */
    void onPerformSteganographyFailure();
  }
}
