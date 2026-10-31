# Medium Part 2 publishing notes

The English article is in [article.md](article.md), continuing the existing English Part 1. It is a local draft prepared from repository snapshot `263f285`; no Medium story has been created or published.

## Quick copy and paste

1. Open [copy-to-medium.html](copy-to-medium.html) in Chrome or Safari.
2. Click **Copy title**, then paste into Medium's title field.
3. Click **Copy subtitle**, then paste into the subtitle line.
4. Click **Copy body**, then paste into the story body with **Command–V**. Use ordinary paste, without Shift, to retain formatting.
5. Check that the GIF and both diagrams appear with their captions. The copied body now includes their public GitHub image references.

The helper copies formatted HTML with headings, links, code, image references, and captions. Medium controls the final formatting, so check these in its draft preview. If an image is omitted during paste, use its public link below to download it, then insert it with Medium's image button or drag and drop. The resulting paste has not been tested in Medium.

## Story fields

- **Title:** Mini-Uber Part 2: Building an Adaptive iPhone Duo UI with Compose Multiplatform
- **Subtitle:** Connecting UIKit reserved regions to shared Kotlin layout logic while preserving selection, scrolling, and navigation intent.
- **Suggested topics:** Kotlin Multiplatform, Compose Multiplatform, iOS Development, Mobile Development, Software Architecture.

## Images and code

The article and copy helper reference these uploaded GitHub images directly:

1. [iOS simulator recording](https://raw.githubusercontent.com/mahdizareeii/Mini-Uber/main/artifacts/adaptive-demo/ios-adaptive.gif): 960×898, 27.6 seconds, 8.27 MB. Preserve the animation.
2. [Architecture diagram](https://raw.githubusercontent.com/mahdizareeii/Mini-Uber/main/artifacts/medium-part-2/architecture.png): native hosts and shared presentation/layout boundaries.
3. [Coordinate pipeline](https://raw.githubusercontent.com/mahdizareeii/Mini-Uber/main/artifacts/medium-part-2/coordinate-pipeline.png): platform coordinates, window conversion, and local pane bounds.

Keep the supplied captions and alt text if you upload an image manually, and retain the SVG files as editable originals. Medium supports GIF and PNG uploads up to 25 MB; the supplied GIF fits that limit. [Medium image guidance](https://help.medium.com/hc/en-us/articles/215679797-Using-images).

Use native code blocks for the ten excerpts, selecting Kotlin or Swift where available. Medium can create a code block through the `{ }` menu or by typing three backticks on an empty line. [Medium code-block guidance](https://help.medium.com/hc/en-us/articles/35756844492439-Using-code-blocks-and-inline-code).

## Final editorial checks

- Keep the Part 1 link in the opening paragraph.
- Confirm the public GitHub links contain the implementation described here.
- Check GIF playback, diagram readability, captions, and code formatting in Medium's draft preview.
- Keep the scope accurate: simulator validation, book posture in the GIF, custom 600dp content breakpoint, and pending app-wide HIG work.
- Publish only after reviewing the finished draft. [Medium draft and publishing guidance](https://help.medium.com/hc/en-us/articles/225168768-Writing-and-publishing-your-first-story).
