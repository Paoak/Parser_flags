package com.pars_test.Parser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SpringBootApplication
public class ParserApplication {

	public static void main(String[] args) throws InterruptedException, IOException {

//		SpringApplication.run(ParserApplication.class, args);

		/**
		 * Имеем список акронимов некоторых стран
		 */

		List<String> countryList = new ArrayList<>();
		countryList.add("ru");
		countryList.add("ua");
		countryList.add("de");
		countryList.add("us");
		countryList.add("gb");
		countryList.add("pe");
		countryList.add("col");

		/**
		 * Преобразуем в массив, а после в строку и убираем пробелы между акронимами
		 */

		String[] arrCountry = countryList.toArray(new String[countryList.size()]);
		String stringCountrys = String.join(" ", arrCountry);
		String stringCountry = stringCountrys.replaceAll(" ",",");

		/**
		 * Конкатенируем полученную строку к гет-запросу с помощью RestTemplate
		 * (заранее создав класс с нужными для нас полями)
		 */

		final RestTemplate restTemplate = new RestTemplate();
		final Country[] countries = restTemplate.getForObject("https://restcountries.com/v2/alpha?codes="+stringCountry,
				Country[].class);
		/**
		 * Разбиваем массив объектов на два
		 */

		Country[] countries1 = Arrays.copyOfRange(countries, 0, countries.length/2);
		Country[] countries2 = Arrays.copyOfRange(countries, countries.length/2, countries.length);

		/**
		 * Создаём два потока, в которые помещаем два массива соответственно. Сначала проверяется наличие директории,
		 * в случае отсутствия, происходит её создание, с последующей загрузкой изображений флагов. Имена файлам
		 * задаются через геттер. Запускаем потоки и готово.
		 */

		File folder = new File("/tmp/countries/flags");

		Thread t1 = new Thread(() -> {
			synchronized (countries1) {
				try {
					if (!folder.exists()) {
						folder.mkdirs();
					}
					for (Country country: countries1){
						ReadableByteChannel readChannel = Channels.newChannel(new URL(country.getFlag()).openStream());
						FileOutputStream fileOS = null;
						fileOS = new FileOutputStream("/tmp/countries/flags/"+country.getName()+".svg");
						FileChannel writeChannel = fileOS.getChannel();
						writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE);
						fileOS.close();
						readChannel.close();
						System.out.println("Downloading " + country.getFlag() +" is successfully in /tmp/countries/flags" +
								" with name " + country.getName());
					}
				} catch (IOException e ) {
					e.printStackTrace();
				}
				}
				System.out.println(
						"t1 writing done successfully");
			});
		Thread t2 = new Thread(() -> {
			synchronized (countries2) {
				try {
					if (!folder.exists()) {
						folder.mkdirs();
					}
					for (Country country: countries2){
						ReadableByteChannel readChannel = Channels.newChannel(new URL(country.getFlag()).openStream());
						FileOutputStream fileOS = null;
						fileOS = new FileOutputStream("/tmp/countries/flags/"+country.getName()+".svg");
						FileChannel writeChannel = fileOS.getChannel();
						writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE);
						fileOS.close();
						readChannel.close();
						System.out.println("Downloading " + country.getFlag() +" is successfully in /tmp/countries/flags" +
								" with name " + country.getName());
					}
				} catch (IOException e ) {
					e.printStackTrace();
				}

			}
			System.out.println(
					"t2 writing done successfully");
		});

		t1.start();
		t2.start();
	}

}



