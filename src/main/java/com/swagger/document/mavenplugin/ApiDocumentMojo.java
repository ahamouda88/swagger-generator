package com.swagger.document.mavenplugin;

import static com.swagger.document.constants.ErrorMessages.*;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.springframework.util.CollectionUtils;

import com.swagger.document.exception.InvalidApiSourceException;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.COMPILE, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME, threadSafe = true)
public class ApiDocumentMojo extends AbstractMojo {

	@Parameter
	private List<ApiSource> apiSources;

	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;

	@Component
	private MavenProjectHelper projectHelper;

	@Parameter(property = "swagger.skip", defaultValue = "false")
	private boolean skipSwaggerGeneration;

	@Parameter(property = "file.encoding")
	private String encoding;

	public void setApiSources(List<ApiSource> apiSources) {
		this.apiSources = apiSources;
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (skipSwaggerGeneration) {
			getLog().info("Swagger generation is skipped.");
			return;
		}

		if (apiSources == null) throw new MojoFailureException(API_SOURCE_MISSING);
//
//		try {
//			getLog().debug(apiSources.toString());
//			for (ApiSource apiSource : apiSources) {
//				validateConfiguration(apiSource);
//				AbstractDocumentSource documentSource = apiSource.isSpringmvc()
//						? new SpringMavenDocumentSource(apiSource, getLog(), projectEncoding)
//						: new MavenDocumentSource(apiSource, getLog(), projectEncoding);
//
//				documentSource.loadTypesToSkip();
//				documentSource.loadModelModifier();
//				documentSource.loadModelConverters();
//				documentSource.loadDocuments();
//				if (apiSource.getOutputPath() != null) {
//					File outputDirectory = new File(apiSource.getOutputPath()).getParentFile();
//					if (outputDirectory != null && !outputDirectory.exists()) {
//						if (!outputDirectory.mkdirs()) {
//							throw new MojoExecutionException(
//									"Create directory[" + apiSource.getOutputPath() + "] for output failed.");
//						}
//					}
//				}
//				if (apiSource.getTemplatePath() != null) {
//					documentSource.toDocuments();
//				}
//				String swaggerFileName = getSwaggerFileName(apiSource.getSwaggerFileName());
//				documentSource.toSwaggerDocuments(
//						apiSource.getSwaggerUIDocBasePath() == null ? apiSource.getBasePath()
//								: apiSource.getSwaggerUIDocBasePath(),
//						apiSource.getOutputFormats(), swaggerFileName, projectEncoding);
//
//				if (apiSource.isAttachSwaggerArtifact() && apiSource.getSwaggerDirectory() != null && project != null) {
//					String outputFormats = apiSource.getOutputFormats();
//					if (outputFormats != null) {
//						for (String format : outputFormats.split(",")) {
//							String classifier = swaggerFileName.equals("swagger")
//									? getSwaggerDirectoryName(apiSource.getSwaggerDirectory())
//									: swaggerFileName;
//							File swaggerFile = new File(apiSource.getSwaggerDirectory(),
//									swaggerFileName + "." + format.toLowerCase());
//							projectHelper.attachArtifact(project, format.toLowerCase(), classifier, swaggerFile);
//						}
//					}
//				}
//			}
//		} catch (InvalidApiSourceException e) {
//			throw new MojoFailureException(e.getMessage(), e);
//		} catch (Exception e) {
//			throw new MojoExecutionException(e.getMessage(), e);
//		}
	}

	private void validateConfiguration(ApiSource apiSource) throws InvalidApiSourceException {
		if (apiSource.getInfo() == null) {
			throw new InvalidApiSourceException(API_SOURCE_MISSING_INFO);
		}
		
		if (apiSource.getInfo().getTitle() == null) {
			throw new InvalidApiSourceException(API_SOURCE_MISSING_INFO_TITLE);
		}

		if (apiSource.getInfo().getVersion() == null) {
			throw new InvalidApiSourceException(API_SOURCE_MISSING_INFO_VERSION);
		}

		if (apiSource.getInfo().getLicense() != null && apiSource.getInfo().getLicense().getName() == null) {
			throw new InvalidApiSourceException(API_SOURCE_MISSING_INFO_LICENSE_NAME);
		}

		if (CollectionUtils.isEmpty(apiSource.getLocations())) {
			throw new InvalidApiSourceException(API_SOURCE_MISSING_LOCATION);
		}
	}

	public List<ApiSource> getApiSources() {
		return apiSources;
	}

	private String getSwaggerFileName(String swaggerFileName) {
		return swaggerFileName == null || "".equals(swaggerFileName.trim()) ? "swagger" : swaggerFileName;
	}

	private String getSwaggerDirectoryName(String swaggerDirectory) {
		return new File(swaggerDirectory).getName();
	}

}
