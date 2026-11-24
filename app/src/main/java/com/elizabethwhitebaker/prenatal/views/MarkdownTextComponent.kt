package com.elizabethwhitebaker.prenatal.views

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.StringAnnotation
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.sumup.markdown.annotatedstring.AnnotatedStringGenerator
import com.sumup.markdown.annotatedstring.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.CancellationToken
import org.intellij.markdown.parser.MarkdownParser

// This is copied from the markdown-annnotatedstring library documentation:
// https://github.com/sumup-oss/markdown-annotatedstring#in-jetpack-compose
@Composable
fun MarkdownTextComponent(
    markdown: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    val flavour = remember { CommonMarkFlavourDescriptor() }
    val annotatedString = remember(markdown) {
        val parser = MarkdownParser(flavour, cancellationToken = CancellationToken.NonCancellable)
        val root = parser.buildMarkdownTreeFromString(markdown as CharSequence)
        val stringGenerator = AnnotatedStringGenerator(markdown, root, flavour)
        // This Markdown library is nice, but for some reason it stores URLs as StringAnnotations
        // and expects the use of the deprecated uriHandler to open tapped links, so let's fix that
        stringGenerator.generateAnnotatedString().mapAnnotations { annotation ->
            when (annotation.item) {
                is StringAnnotation -> {
                    if (annotation.tag == "URL") {
                        val link = (annotation.item as StringAnnotation).value
                        val linkAnnotation = LinkAnnotation.Url(link)
                        AnnotatedString.Range(
                            linkAnnotation,
                            annotation.start,
                            annotation.end/*, annotation.tag*/
                        )
                    } else annotation
                }

                else -> annotation
            }
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier,
        // I can't think of any text we wouldn't want centered
        textAlign = TextAlign.Center,
        style = style
    )
}