/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.vulcan.writer;

import static com.liferay.vulcan.test.list.FunctionalListMatchers.aFunctionalListThat;

import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.collection.IsMapWithSize.aMapWithSize;
import static org.hamcrest.collection.IsMapWithSize.anEmptyMap;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

import com.liferay.vulcan.language.Language;
import com.liferay.vulcan.list.FunctionalList;
import com.liferay.vulcan.pagination.SingleModel;
import com.liferay.vulcan.request.RequestInfo;
import com.liferay.vulcan.resource.RelatedModel;
import com.liferay.vulcan.resource.identifier.StringIdentifier;
import com.liferay.vulcan.test.resource.MockRepresentorCreator;
import com.liferay.vulcan.test.resource.model.FirstEmbeddedModel;
import com.liferay.vulcan.test.resource.model.RootModel;
import com.liferay.vulcan.uri.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Alejandro Hernández
 */
@RunWith(MockitoJUnitRunner.class)
public class FieldsWriterTest {

	@Before
	public void setUp() {
		Mockito.when(
			_requestInfo.getServerURL()
		).thenReturn(
			() -> "www.liferay.com"
		);

		Mockito.when(
			_requestInfo.getFieldsOptional()
		).thenReturn(
			Optional.empty()
		);

		Mockito.when(
			_requestInfo.getEmbeddedOptional()
		).thenReturn(
			Optional.empty()
		);

		Mockito.when(
			_requestInfo.getLanguageOptional()
		).thenReturn(
			Optional.of(Mockito.mock(Language.class))
		);

		_fieldsWriter = new FieldsWriter<>(
			new SingleModel<>(() -> "first", RootModel.class), _requestInfo,
			MockRepresentorCreator.createRootModelRepresentor(true),
			new Path("name", "id"), new FunctionalList<>(null, "first"));
	}

	@Test
	public void testGetSingleModel() {
		SingleModel<Integer> parentSingleModel = new SingleModel<>(
			3, Integer.class);

		RelatedModel<Integer, String> relatedModel = new RelatedModel<>(
			"key", String.class,
			integer -> Optional.of(String.valueOf(integer)));

		Optional<SingleModel<String>> optional = FieldsWriter.getSingleModel(
			relatedModel, parentSingleModel);

		assertThat(optional, is(optionalWithValue()));

		optional.ifPresent(
			singleModel -> {
				assertThat(
					singleModel.getModelClass(), is(equalTo(String.class)));

				assertThat(singleModel.getModel(), is(equalTo("3")));
			});
	}

	@Test
	public void testWriteBinaries() {
		Map<String, String> binaries = new HashMap<>();

		_fieldsWriter.writeBinaries(binaries::put);

		assertThat(binaries, is(aMapWithSize(2)));
		assertThat(
			binaries, hasEntry("binary1", "www.liferay.com/b/name/id/binary1"));
		assertThat(
			binaries, hasEntry("binary2", "www.liferay.com/b/name/id/binary2"));
	}

	@Test
	public void testWriteBinariesWithFieldsFilter() {
		Mockito.when(
			_requestInfo.getFieldsOptional()
		).thenReturn(
			Optional.of(list -> "binary2"::equals)
		);

		Map<String, String> binaries = new HashMap<>();

		_fieldsWriter.writeBinaries(binaries::put);

		assertThat(binaries, is(aMapWithSize(1)));
		assertThat(
			binaries, hasEntry("binary2", "www.liferay.com/b/name/id/binary2"));
	}

	@Test
	public void testWriteBooleanFields() {
		Map<String, Boolean> booleans = new HashMap<>();

		_fieldsWriter.writeBooleanFields(booleans::put);

		assertThat(booleans, is(aMapWithSize(2)));
		assertThat(booleans, hasEntry("boolean1", true));
		assertThat(booleans, hasEntry("boolean2", false));
	}

	@Test
	public void testWriteBooleanFieldsWithFieldsFilter() {
		Mockito.when(
			_requestInfo.getFieldsOptional()
		).thenReturn(
			Optional.of(list -> "boolean2"::equals)
		);

		Map<String, Boolean> booleans = new HashMap<>();

		_fieldsWriter.writeBooleanFields(booleans::put);

		assertThat(booleans, is(aMapWithSize(1)));
		assertThat(booleans, hasEntry("boolean2", false));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testWriteEmbeddedRelatedModelsWithEmbeddedPredicate() {
		List<String> linkedRelatedModelURLs = new ArrayList<>();
		List<String> embeddedRelatedModelURLs = new ArrayList<>();
		List<FunctionalList<String>> firstEmbeddedPathElementsList =
			new ArrayList<>();
		List<FunctionalList<String>> secondEmbeddedPathElementsList =
			new ArrayList<>();
		List<FunctionalList<String>> thirdEmbeddedPathElementsList =
			new ArrayList<>();
		List<SingleModel> singleModels = new ArrayList<>();

		Function<SingleModel<?>, Optional<Path>> pathFunction = Mockito.mock(
			Function.class);

		Mockito.when(
			_requestInfo.getEmbeddedOptional()
		).thenReturn(
			Optional.of(() -> "first.embedded2"::equals)
		);

		Mockito.when(
			pathFunction.apply(Mockito.any())
		).thenReturn(
			Optional.of(new Path("name1", "id1")),
			Optional.of(new Path("name2", "id2"))
		);

		_fieldsWriter.writeEmbeddedRelatedModels(
			pathFunction,
			(singleModel, embeddedPathElements) -> {
				singleModels.add(singleModel);
				firstEmbeddedPathElementsList.add(embeddedPathElements);
			},
			(url, embeddedPathElements) -> {
				linkedRelatedModelURLs.add(url);
				secondEmbeddedPathElementsList.add(embeddedPathElements);
			},
			(url, embeddedPathElements) -> {
				embeddedRelatedModelURLs.add(url);
				thirdEmbeddedPathElementsList.add(embeddedPathElements);
			});

		assertThat(singleModels, hasSize(equalTo(1)));

		SingleModel singleModel = singleModels.get(0);

		assertThat(
			singleModel.getModel(), is(instanceOf(FirstEmbeddedModel.class)));

		assertThat(linkedRelatedModelURLs, hasSize(equalTo(1)));
		assertThat(
			linkedRelatedModelURLs, contains("www.liferay.com/p/name1/id1"));

		assertThat(embeddedRelatedModelURLs, hasSize(equalTo(1)));
		assertThat(
			embeddedRelatedModelURLs, contains("www.liferay.com/p/name2/id2"));

		assertThat(firstEmbeddedPathElementsList, hasSize(equalTo(1)));
		assertThat(
			firstEmbeddedPathElementsList,
			contains(aFunctionalListThat(contains("first", "embedded2"))));

		assertThat(secondEmbeddedPathElementsList, hasSize(equalTo(1)));
		assertThat(
			secondEmbeddedPathElementsList,
			contains(aFunctionalListThat(contains("first", "embedded1"))));

		assertThat(thirdEmbeddedPathElementsList, hasSize(equalTo(1)));
		assertThat(
			thirdEmbeddedPathElementsList,
			contains(aFunctionalListThat(contains("first", "embedded2"))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testWriteEmbeddedRelatedModelsWithFieldsFilter() {
		Mockito.when(
			_requestInfo.getFieldsOptional()
		).thenReturn(
			Optional.of(list -> "embedded2"::equals)
		);

		List<String> linkedRelatedModelURLs = new ArrayList<>();
		List<FunctionalList<String>> linkedPathElementsList = new ArrayList<>();

		Function<SingleModel<?>, Optional<Path>> pathFunction = Mockito.mock(
			Function.class);

		Mockito.when(
			pathFunction.apply(Mockito.any())
		).thenReturn(
			Optional.of(new Path("name2", "id2"))
		);

		_fieldsWriter.writeEmbeddedRelatedModels(
			pathFunction,
			(singleModel, embeddedPathElements) ->
				Assert.fail("Shouldn't be embedded"),
			(url, embeddedPathElements) -> {
				linkedRelatedModelURLs.add(url);
				linkedPathElementsList.add(embeddedPathElements);
			},
			(url, embeddedPathElements) -> Assert.fail(
				"Shouldn't be embedded"));

		assertThat(linkedRelatedModelURLs, hasSize(equalTo(1)));
		assertThat(
			linkedRelatedModelURLs, contains("www.liferay.com/p/name2/id2"));

		assertThat(
			linkedPathElementsList,
			contains(aFunctionalListThat(contains("first", "embedded2"))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testWriteEmbeddedRelatedModelsWithoutEmbeddedPredicate() {
		List<String> linkedRelatedModelURLs = new ArrayList<>();
		List<FunctionalList<String>> embeddedPathElementsList =
			new ArrayList<>();

		Function<SingleModel<?>, Optional<Path>> pathFunction = Mockito.mock(
			Function.class);

		Mockito.when(
			pathFunction.apply(Mockito.any())
		).thenReturn(
			Optional.of(new Path("name1", "id1")),
			Optional.of(new Path("name2", "id2"))
		);

		_fieldsWriter.writeEmbeddedRelatedModels(
			pathFunction,
			(singleModel, embeddedPathElements) ->
				Assert.fail("Shouldn't be embedded"),
			(url, embeddedPathElements) -> {
				linkedRelatedModelURLs.add(url);
				embeddedPathElementsList.add(embeddedPathElements);
			},
			(url, embeddedPathElements) -> Assert.fail(
				"Shouldn't be embedded"));

		assertThat(linkedRelatedModelURLs, hasSize(equalTo(2)));
		assertThat(
			linkedRelatedModelURLs,
			contains(
				"www.liferay.com/p/name1/id1", "www.liferay.com/p/name2/id2"));

		assertThat(
			embeddedPathElementsList,
			contains(
				aFunctionalListThat(contains("first", "embedded1")),
				aFunctionalListThat(contains("first", "embedded2"))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testWriteLinkedRelatedModels() {
		List<String> linkedRelatedModelsURLs = new ArrayList<>();
		List<FunctionalList<String>> embeddedPathElementsList =
			new ArrayList<>();

		Function<SingleModel<?>, Optional<Path>> pathFunction = Mockito.mock(
			Function.class);

		Mockito.when(
			pathFunction.apply(Mockito.any())
		).thenReturn(
			Optional.of(new Path("name1", "id1")),
			Optional.of(new Path("name2", "id2"))
		);

		_fieldsWriter.writeLinkedRelatedModels(
			pathFunction,
			(url, embeddedPathElements) -> {
				linkedRelatedModelsURLs.add(url);
				embeddedPathElementsList.add(embeddedPathElements);
			});

		assertThat(linkedRelatedModelsURLs, hasSize(equalTo(2)));
		assertThat(
			linkedRelatedModelsURLs,
			contains(
				"www.liferay.com/p/name1/id1", "www.liferay.com/p/name2/id2"));

		assertThat(
			embeddedPathElementsList,
			contains(
				aFunctionalListThat(contains("first", "linked1")),
				aFunctionalListThat(contains("first", "linked2"))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testWriteLinkedRelatedModelsWithFieldsFilter() {
		Mockito.when(
			_requestInfo.getFieldsOptional()
		).thenReturn(
			Optional.of(list -> "linked2"::equals)
		);

		List<String> linkedRelatedModelsURLs = new ArrayList<>();
		List<FunctionalList<String>> embeddedPathElementsList =
			new ArrayList<>();

		Function<SingleModel<?>, Optional<Path>> pathFunction = Mockito.mock(
			Function.class);

		Mockito.when(
			pathFunction.apply(Mockito.any())
		).thenReturn(
			Optional.of(new Path("name2", "id2"))
		);

		_fieldsWriter.writeLinkedRelatedModels(
			pathFunction,
			(url, embeddedPathElements) -> {
				linkedRelatedModelsURLs.add(url);
				embeddedPathElementsList.add(embeddedPathElements);
			});

		assertThat(linkedRelatedModelsURLs, hasSize(equalTo(1)));
		assertThat(
			linkedRelatedModelsURLs, contains("www.liferay.com/p/name2/id2"));

		assertThat(
			embeddedPathElementsList,
			contains(aFunctionalListThat(contains("first", "linked2"))));
	}

	@Test
	public void testWriteLinks() {
		Map<String, String> links = new HashMap<>();

		_fieldsWriter.writeLinks(links::put);

		assertThat(links, is(aMapWithSize(2)));
		assertThat(links, hasEntry("link1", "www.liferay.com"));
		assertThat(links, hasEntry("link2", "community.liferay.com"));
	}

	@Test
	public void testWriteLinksWithFilter() {
		Mockito.when(
			_requestInfo.getFieldsOptional()
		).thenReturn(
			Optional.of(list -> "link2"::equals)
		);

		Map<String, String> links = new HashMap<>();

		_fieldsWriter.writeLinks(links::put);

		assertThat(links, is(aMapWithSize(1)));
		assertThat(links, hasEntry("link2", "community.liferay.com"));
	}

	@Test
	public void testWriteLocalizedStringFailsIfNoLanguageIsProvided() {
		Mockito.when(
			_requestInfo.getLanguageOptional()
		).thenReturn(
			Optional.empty()
		);

		Map<String, String> localizedStrings = new HashMap<>();

		_fieldsWriter.writeLocalizedStringFields(localizedStrings::put);

		assertThat(localizedStrings, is(anEmptyMap()));
	}

	@Test
	public void testWriteLocalizedStringFields() {
		Map<String, String> localizedStrings = new HashMap<>();

		_fieldsWriter.writeLocalizedStringFields(localizedStrings::put);

		assertThat(localizedStrings, is(aMapWithSize(2)));
		assertThat(
			localizedStrings, hasEntry("localizedString1", "Translated 1"));
		assertThat(
			localizedStrings, hasEntry("localizedString2", "Translated 2"));
	}

	@Test
	public void testWriteLocalizedStringFieldsWithFilter() {
		Mockito.when(
			_requestInfo.getFieldsOptional()
		).thenReturn(
			Optional.of(list -> "localizedString2"::equals)
		);

		Map<String, String> localizedStrings = new HashMap<>();

		_fieldsWriter.writeLocalizedStringFields(localizedStrings::put);

		assertThat(localizedStrings, is(aMapWithSize(1)));
		assertThat(
			localizedStrings, hasEntry("localizedString2", "Translated 2"));
	}

	@Test
	public void testWriteNumberFields() {
		Map<String, Number> numbers = new HashMap<>();

		_fieldsWriter.writeNumberFields(numbers::put);

		assertThat(numbers, is(aMapWithSize(2)));
		assertThat(numbers, hasEntry("number1", 2017));
		assertThat(numbers, hasEntry("number2", 42));
	}

	@Test
	public void testWriteNumberFieldsWithFilter() {
		Mockito.when(
			_requestInfo.getFieldsOptional()
		).thenReturn(
			Optional.of(list -> "number2"::equals)
		);

		Map<String, Number> numbers = new HashMap<>();

		_fieldsWriter.writeNumberFields(numbers::put);

		assertThat(numbers, is(aMapWithSize(1)));
		assertThat(numbers, hasEntry("number2", 42));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testWriteRelatedCollections() {
		List<String> relatedCollectionURLs = new ArrayList<>();
		List<FunctionalList<String>> embeddedPathElementsList =
			new ArrayList<>();

		Function<String, Optional<String>> nameFunction = Mockito.mock(
			Function.class);

		Mockito.when(
			nameFunction.apply(Mockito.any())
		).thenReturn(
			Optional.of("first"), Optional.of("second")
		);

		_fieldsWriter.writeRelatedCollections(
			nameFunction,
			(url, embeddedPathElements) -> {
				relatedCollectionURLs.add(url);
				embeddedPathElementsList.add(embeddedPathElements);
			});

		assertThat(relatedCollectionURLs, hasSize(equalTo(2)));
		assertThat(
			relatedCollectionURLs,
			contains(
				"www.liferay.com/p/name/id/first",
				"www.liferay.com/p/name/id/second"));

		assertThat(
			embeddedPathElementsList,
			contains(
				aFunctionalListThat(contains("first", "relatedCollection1")),
				aFunctionalListThat(contains("first", "relatedCollection2"))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testWriteRelatedCollectionsWithFilter() {
		Mockito.when(
			_requestInfo.getFieldsOptional()
		).thenReturn(
			Optional.of(list -> "relatedCollection2"::equals)
		);

		List<String> relatedCollectionURLs = new ArrayList<>();
		List<FunctionalList<String>> embeddedPathElementsList =
			new ArrayList<>();

		Function<String, Optional<String>> nameFunction = Mockito.mock(
			Function.class);

		Mockito.when(
			nameFunction.apply(Mockito.any())
		).thenReturn(
			Optional.of("first"), Optional.of("second")
		);

		_fieldsWriter.writeRelatedCollections(
			nameFunction,
			(url, embeddedPathElements) -> {
				relatedCollectionURLs.add(url);
				embeddedPathElementsList.add(embeddedPathElements);
			});

		assertThat(relatedCollectionURLs, hasSize(equalTo(1)));
		assertThat(
			relatedCollectionURLs,
			contains("www.liferay.com/p/name/id/second"));

		assertThat(
			embeddedPathElementsList,
			contains(
				aFunctionalListThat(contains("first", "relatedCollection2"))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testWriteRelatedModelFailsIfEmptyRelatedModel() {
		RelatedModel<RootModel, Integer> relatedModel = new RelatedModel<>(
			"key", Integer.class, __ -> Optional.empty());

		Function<SingleModel<?>, Optional<Path>> pathFunction = Mockito.mock(
			Function.class);

		Mockito.when(
			pathFunction.apply(Mockito.any())
		).thenReturn(
			Optional.of(new Path("name1", "id1"))
		);
		_fieldsWriter.writeEmbeddedRelatedModel(
			relatedModel, pathFunction,
			(singleModel, embeddedPathElements) ->
				Assert.fail("Shouldn't be called"),
			(url, embeddedPathElements) -> Assert.fail("Shouldn't be called"),
			(url, embeddedPathElements) -> Assert.fail("Shouldn't be called"));
	}

	@Test
	public void testWriteSingleURL() {
		_fieldsWriter.writeSingleURL(
			url -> assertThat(url, is(equalTo("www.liferay.com/p/name/id"))));
	}

	@Test
	public void testWriteStringFields() {
		Map<String, String> strings = new HashMap<>();

		_fieldsWriter.writeStringFields(strings::put);

		assertThat(strings, is(aMapWithSize(4)));
		assertThat(strings, hasEntry("string1", "Live long and prosper"));
		assertThat(strings, hasEntry("string2", "Hypermedia"));
		assertThat(strings, hasEntry("date1", "2016-06-15T09:00Z"));
		assertThat(strings, hasEntry("date2", "2017-04-03T18:36Z"));
	}

	@Test
	public void testWriteStringFieldsWithFilter() {
		Mockito.when(
			_requestInfo.getFieldsOptional()
		).thenReturn(
			Optional.of(list -> "string2"::equals)
		);

		Map<String, String> strings = new HashMap<>();

		_fieldsWriter.writeStringFields(strings::put);

		assertThat(strings, is(aMapWithSize(1)));
		assertThat(strings, hasEntry("string2", "Hypermedia"));
	}

	@Test
	public void testWriteTypes() {
		List<String> types = new ArrayList<>();

		_fieldsWriter.writeTypes(types::addAll);

		assertThat(types, contains("Type 1", "Type 2"));
	}

	private FieldsWriter<RootModel, StringIdentifier> _fieldsWriter;
	private final RequestInfo _requestInfo = Mockito.mock(RequestInfo.class);

}