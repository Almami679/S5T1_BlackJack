//package S5T1BlackJack.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class JpaConfig {
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource);
//        em.setPackagesToScan("S5T1BlackJack.entities.sql");
//        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//        return em;
//    }
//}

