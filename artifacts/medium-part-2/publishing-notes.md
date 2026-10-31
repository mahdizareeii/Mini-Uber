# Medium Part 2 publishing notes

The English article is in [article.md](article.md), continuing the existing English Part 1. It is a local draft prepared from repository snapshot `263f285`; no Medium story has been created or published.

## Quick copy and paste

1. Open [copy-to-medium.html](copy-to-medium.html) in Chrome or Safari.
2. Click **Copy title**, then paste into Medium's title field.
3. Click **Copy subtitle**, then paste into the subtitle line.
4. Click **Copy body**, then paste into the story body with **Command–V**. Use ordinary paste, without Shift, to retain formatting.
5. Replace the three **UPLOAD IMAGE** markers with the GIF and two PNGs listed below, using Medium's image button or drag and drop.

The helper copies formatted HTML so headings, links, and code can retain their structure. Medium controls the final formatting, so check the code blocks and image captions in its draft preview. The resulting paste has not been tested in Medium.

## Story fields

- **Title:** Mini-Uber Part 2: Building an Adaptive iPhone Duo UI with Compose Multiplatform
- **Subtitle:** Connecting UIKit reserved regions to shared Kotlin layout logic while preserving selection, scrolling, and navigation intent.
- **Suggested topics:** Kotlin Multiplatform, Compose Multiplatform, iOS Development, Mobile Development, Software Architecture.

## Images and code

Insert the three images individually at their positions in the article and use the supplied captions and alt text:

1. [iOS simulator recording](../adaptive-demo/ios-adaptive.gif): 960×898, 27.6 seconds, 8.27 MB. Preserve the animation.
2. [Architecture diagram](architecture.png): native hosts and shared presentation/layout boundaries.
3. [Coordinate pipeline](coordinate-pipeline.png): platform coordinates, window conversion, and local pane bounds.

Upload the PNG figures to Medium; retain the SVG files as editable originals. The relative image links in the Markdown are local draft references and need to become uploaded images in the story. Medium supports GIF and PNG uploads up to 25 MB; the supplied GIF fits that limit. [Medium image guidance](https://help.medium.com/hc/en-us/articles/215679797-Using-images).

Use native code blocks for the ten excerpts, selecting Kotlin or Swift where available. Medium can create a code block through the `{ }` menu or by typing three backticks on an empty line. [Medium code-block guidance](https://help.medium.com/hc/en-us/articles/35756844492439-Using-code-blocks-and-inline-code).

## Final editorial checks

- Keep the Part 1 link in the opening paragraph.
- Confirm the public GitHub links contain the implementation described here.
- Check GIF playback, diagram readability, captions, and code formatting in Medium's draft preview.
- Keep the scope accurate: simulator validation, book posture in the GIF, custom 600dp content breakpoint, and pending app-wide HIG work.
- Publish only after reviewing the finished draft. [Medium draft and publishing guidance](https://help.medium.com/hc/en-us/articles/225168768-Writing-and-publishing-your-first-story).
