package com.example.application.employee.mapper;

import java.util.stream.Stream;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeUpdateVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EmployeeApplicationMapperImplTest {

  private final EmployeeApplicationMapper mapper = new EmployeeApplicationMapperImpl();

  @ParameterizedTest
  @MethodSource("createCmdToVOParameters")
  @DisplayName("Mapping EmployeeCreateCmd to EmployeeNameVO correctly")
  void mapCreateCmdToEmployeeNameVOTest(EmployeeNameVO expected, EmployeeCreateCmd employeeCreateCmd) {
    EmployeeNameVO result = mapper.mapToEmployeeNameVO(employeeCreateCmd);
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @MethodSource("byNameQueryToVOParameters")
  @DisplayName("Mapping EmployeeByNameQuery to EmployeeNameVO correctly")
  void mapByNameQueryToEmployeeNameVOTest(EmployeeNameVO expected, EmployeeByNameQuery employeeByNameQuery) {
    EmployeeNameVO result = mapper.mapToEmployeeNameVO(employeeByNameQuery);
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @MethodSource("updateCmdToVOParameters")
  @DisplayName("Mapping EmployeeUpdateCmd to EmployeeUpdateVO correctly")
  void mapToEmployeeUpdateVOTest(EmployeeUpdateVO expected, EmployeeUpdateCmd employeeUpdateCmd) {
    EmployeeUpdateVO result = mapper.mapToEmployeeUpdateVO(employeeUpdateCmd);
    Assertions.assertEquals(expected, result);
  }

  private static Stream<Arguments> createCmdToVOParameters() {
    return Stream.of(
        Arguments.of(
            EmployeeNameVO.builder().name("Walter").build(),
            new EmployeeCreateCmd("Walter")
        ),
        Arguments.of(
            EmployeeNameVO.builder().name(null).build(),
            new EmployeeCreateCmd(null)
        )
    );
  }

  private static Stream<Arguments> byNameQueryToVOParameters() {
    return Stream.of(
        Arguments.of(
            EmployeeNameVO.builder().name("Walter").build(),
            new EmployeeByNameQuery("Walter")
        ),
        Arguments.of(
            EmployeeNameVO.builder().name(null).build(),
            new EmployeeByNameQuery(null)
        )
    );
  }

  private static Stream<Arguments> updateCmdToVOParameters() {
    return Stream.of(
        Arguments.of(
            EmployeeUpdateVO.builder().number(1L).name("Walter").build(),
            new EmployeeUpdateCmd(1L, "Walter")
        ),
        Arguments.of(
            EmployeeUpdateVO.builder().number(1L).name(null).build(),
            new EmployeeUpdateCmd(1L, null)
        )
    );
  }
}